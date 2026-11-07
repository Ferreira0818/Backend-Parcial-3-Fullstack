package com.poe.ecommerce.transacciones.service.impl;

import com.poe.ecommerce.transacciones.client.ProductoClient;
import com.poe.ecommerce.transacciones.client.UsuarioClient;
import com.poe.ecommerce.transacciones.dto.*;
import com.poe.ecommerce.transacciones.exception.BadRequestException;
import com.poe.ecommerce.transacciones.exception.ResourceNotFoundException;
import com.poe.ecommerce.transacciones.exception.TransaccionInvalidaException;
import com.poe.ecommerce.transacciones.model.*;
import com.poe.ecommerce.transacciones.repository.EstadoRepository;
import com.poe.ecommerce.transacciones.repository.MedioDePagoRepository;
import com.poe.ecommerce.transacciones.repository.TransaccionRepository;
import com.poe.ecommerce.transacciones.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final EstadoRepository estadoRepository;
    private final MedioDePagoRepository medioDePagoRepository;
    private final UsuarioClient usuarioClient;
    private final ProductoClient productoClient;

    public static final String ESTADO_EN_CARRITO = "EN_CARRITO";
    public static final String ESTADO_PAGADO = "PAGADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";

    @Override
    @Transactional
    public Transaccion obtenerOCrearCarrito(CrearCarritoRequestDTO request) {
        // 1. Validar que el usuario exista en servicio-usuarios mediante WebClient
        UsuarioExternoDTO usuario = usuarioClient.obtenerUsuarioPorId(request.getIdUsuario());
        if (usuario == null) {
            throw new ResourceNotFoundException("El usuario con ID " + request.getIdUsuario() + " no fue encontrado");
        }

        // 2. Buscar si ya tiene un carrito activo
        Optional<Transaccion> carritoExistente = transaccionRepository
                .findByIdUsuarioAndEstadoNombreEstado(request.getIdUsuario(), ESTADO_EN_CARRITO);

        Transaccion transaccion;
        if (carritoExistente.isPresent()) {
            transaccion = carritoExistente.get();
        } else {
            Estado estadoCarrito = estadoRepository.findByNombreEstado(ESTADO_EN_CARRITO)
                    .orElseThrow(() -> new ResourceNotFoundException("Estado EN_CARRITO no configurado en la base de datos"));

            transaccion = Transaccion.builder()
                    .idUsuario(request.getIdUsuario())
                    .estado(estadoCarrito)
                    .total(0.0)
                    .medioDePago(null) // Permite NULL mientras esté EN_CARRITO
                    .items(new ArrayList<>())
                    .build();

            transaccion = transaccionRepository.save(transaccion);
        }

        // Si se enviaron ítems iniciales, agregarlos
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (ItemCarritoRequestDTO item : request.getItems()) {
                agregarOActualizarItem(transaccion.getIdTransaccion(), item);
            }
            transaccion = obtenerPorId(transaccion.getIdTransaccion());
        }

        return transaccion;
    }

    @Override
    @Transactional
    public Transaccion agregarOActualizarItem(Integer idTransaccion, ItemCarritoRequestDTO itemRequest) {
        Transaccion transaccion = obtenerPorId(idTransaccion);
        validarEstadoEnCarrito(transaccion);

        // Validar existencia y stock del producto en servicio-productos mediante WebClient
        ProductoExternoDTO producto = productoClient.obtenerProductoPorId(itemRequest.getProductoId());
        if (producto == null) {
            throw new ResourceNotFoundException("El producto con ID " + itemRequest.getProductoId() + " no existe");
        }

        if (producto.getStock() < itemRequest.getCantidad()) {
            throw new TransaccionInvalidaException("Stock insuficiente para el producto '" +
                    producto.getNombreProducto() + "'. Stock disponible: " + producto.getStock() +
                    ", solicitado: " + itemRequest.getCantidad());
        }

        // Buscar si el ítem ya está en el carrito
        Optional<TransaccionProducto> itemExistente = transaccion.getItems().stream()
                .filter(i -> i.getId().getProductoId().equals(itemRequest.getProductoId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            TransaccionProducto item = itemExistente.get();
            item.setCantidadProducto(itemRequest.getCantidad());
            item.setPrecioProducto(producto.getPrecio()); // Asegurar precio oficial actualizado
        } else {
            TransaccionProducto nuevoItem = TransaccionProducto.builder()
                    .id(new TransaccionProductoId(transaccion.getIdTransaccion(), producto.getIdProducto()))
                    .transaccion(transaccion)
                    .precioProducto(producto.getPrecio())
                    .cantidadProducto(itemRequest.getCantidad())
                    .build();
            transaccion.getItems().add(nuevoItem);
        }

        recalcularTotal(transaccion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    @Transactional
    public Transaccion eliminarItem(Integer idTransaccion, Integer productoId) {
        Transaccion transaccion = obtenerPorId(idTransaccion);
        validarEstadoEnCarrito(transaccion);

        boolean removido = transaccion.getItems().removeIf(
                item -> item.getId().getProductoId().equals(productoId)
        );

        if (!removido) {
            throw new ResourceNotFoundException("El producto con ID " + productoId + " no se encuentra en el carrito");
        }

        recalcularTotal(transaccion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    @Transactional
    public Transaccion checkout(Integer idTransaccion, CheckoutRequestDTO checkoutRequest) {
        Transaccion transaccion = obtenerPorId(idTransaccion);
        validarEstadoEnCarrito(transaccion);

        if (transaccion.getItems().isEmpty()) {
            throw new TransaccionInvalidaException("No se puede realizar el checkout de un carrito vacío");
        }

        // 1. Validar y asignar medio de pago
        MedioDePago medioDePago = medioDePagoRepository.findById(checkoutRequest.getIdMedioDePago())
                .orElseThrow(() -> new ResourceNotFoundException("Medio de pago con ID " + checkoutRequest.getIdMedioDePago() + " no encontrado"));

        // 2. Validar y descontar stock síncronamente en servicio-productos para cada ítem
        for (TransaccionProducto item : transaccion.getItems()) {
            productoClient.descontarStock(item.getId().getProductoId(), item.getCantidadProducto());
        }

        // 3. Cambiar estado a PAGADO y asociar medio de pago
        Estado estadoPagado = estadoRepository.findByNombreEstado(ESTADO_PAGADO)
                .orElseThrow(() -> new ResourceNotFoundException("Estado PAGADO no configurado"));

        transaccion.setMedioDePago(medioDePago);
        transaccion.setEstado(estadoPagado);
        recalcularTotal(transaccion);

        log.info("Checkout completado exitosamente para Transacción ID: {}, Total: ${}", transaccion.getIdTransaccion(), transaccion.getTotal());
        return transaccionRepository.save(transaccion);
    }

    @Override
    @Transactional
    public Transaccion cancelarTransaccion(Integer idTransaccion) {
        Transaccion transaccion = obtenerPorId(idTransaccion);

        if (transaccion.getEstado().getNombreEstado().equalsIgnoreCase(ESTADO_CANCELADO)) {
            throw new BadRequestException("La transacción ya se encuentra cancelada");
        }
        if (transaccion.getEstado().getNombreEstado().equalsIgnoreCase(ESTADO_PAGADO)) {
            throw new BadRequestException("No se puede cancelar una transacción que ya ha sido pagada");
        }

        Estado estadoCancelado = estadoRepository.findByNombreEstado(ESTADO_CANCELADO)
                .orElseThrow(() -> new ResourceNotFoundException("Estado CANCELADO no configurado"));

        transaccion.setEstado(estadoCancelado);
        return transaccionRepository.save(transaccion);
    }

    @Override
    @Transactional(readOnly = true)
    public Transaccion obtenerPorId(Integer id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transacción con id " + id + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public TransaccionResponseDTO obtenerDtoPorId(Integer id) {
        return mapearAResponseDTO(obtenerPorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> listarTodas(Integer idUsuario, String estado) {
        if (idUsuario != null && estado != null) {
            return transaccionRepository.findByIdUsuarioAndEstadoNombreEstado(idUsuario, estado)
                    .map(List::of)
                    .orElse(List.of());
        } else if (idUsuario != null) {
            return transaccionRepository.findByIdUsuario(idUsuario);
        } else if (estado != null) {
            return transaccionRepository.findByEstadoNombreEstado(estado);
        }
        return transaccionRepository.findAll();
    }

    @Override
    public TransaccionResponseDTO mapearAResponseDTO(Transaccion transaccion) {
        // Enriquecer con datos del usuario de forma resiliente
        UsuarioExternoDTO usuario = null;
        try {
            usuario = usuarioClient.obtenerUsuarioPorId(transaccion.getIdUsuario());
        } catch (Exception e) {
            log.warn("No fue posible enriquecer los datos del usuario ID: {}", transaccion.getIdUsuario());
        }

        List<ItemTransaccionResponseDTO> itemsDTO = transaccion.getItems().stream()
                .map(item -> {
                    String nombreProducto = "Producto ID " + item.getId().getProductoId();
                    try {
                        ProductoExternoDTO p = productoClient.obtenerProductoPorId(item.getId().getProductoId());
                        if (p != null) {
                            nombreProducto = p.getNombreProducto();
                        }
                    } catch (Exception e) {
                        log.warn("No fue posible obtener el nombre del producto ID: {}", item.getId().getProductoId());
                    }

                    double subtotal = item.getPrecioProducto() * item.getCantidadProducto();
                    return ItemTransaccionResponseDTO.builder()
                            .productoId(item.getId().getProductoId())
                            .nombreProducto(nombreProducto)
                            .precioProducto(item.getPrecioProducto())
                            .cantidadProducto(item.getCantidadProducto())
                            .subtotal(Math.round(subtotal * 100.0) / 100.0)
                            .build();
                })
                .collect(Collectors.toList());

        return TransaccionResponseDTO.builder()
                .idTransaccion(transaccion.getIdTransaccion())
                .idUsuario(transaccion.getIdUsuario())
                .usuario(usuario)
                .total(Math.round(transaccion.getTotal() * 100.0) / 100.0)
                .estado(transaccion.getEstado() != null ? transaccion.getEstado().getNombreEstado() : null)
                .fechaTransaccion(transaccion.getFechaTransaccion())
                .medioDePago(transaccion.getMedioDePago() != null ? transaccion.getMedioDePago().getNombreMedioDePago() : null)
                .items(itemsDTO)
                .build();
    }

    private void validarEstadoEnCarrito(Transaccion transaccion) {
        if (!transaccion.getEstado().getNombreEstado().equalsIgnoreCase(ESTADO_EN_CARRITO)) {
            throw new TransaccionInvalidaException("Solo se pueden modificar o procesar transacciones en estado 'EN_CARRITO'. Estado actual: " +
                    transaccion.getEstado().getNombreEstado());
        }
    }

    private void recalcularTotal(Transaccion transaccion) {
        double total = transaccion.getItems().stream()
                .mapToDouble(i -> i.getPrecioProducto() * i.getCantidadProducto())
                .sum();
        transaccion.setTotal(Math.round(total * 100.0) / 100.0);
    }
}
