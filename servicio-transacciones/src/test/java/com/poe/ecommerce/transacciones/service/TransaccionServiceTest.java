package com.poe.ecommerce.transacciones.service;

import com.poe.ecommerce.transacciones.client.ProductoClient;
import com.poe.ecommerce.transacciones.client.UsuarioClient;
import com.poe.ecommerce.transacciones.dto.*;
import com.poe.ecommerce.transacciones.exception.ResourceNotFoundException;
import com.poe.ecommerce.transacciones.exception.TransaccionInvalidaException;
import com.poe.ecommerce.transacciones.model.*;
import com.poe.ecommerce.transacciones.repository.EstadoRepository;
import com.poe.ecommerce.transacciones.repository.MedioDePagoRepository;
import com.poe.ecommerce.transacciones.repository.TransaccionRepository;
import com.poe.ecommerce.transacciones.service.impl.TransaccionServiceImpl;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private MedioDePagoRepository medioDePagoRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private Faker faker;
    private Estado estadoEnCarrito;
    private Estado estadoPagado;
    private MedioDePago medioDePagoTarjeta;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        estadoEnCarrito = Estado.builder().idEstado(1).nombreEstado("EN_CARRITO").build();
        estadoPagado = Estado.builder().idEstado(2).nombreEstado("PAGADO").build();
        medioDePagoTarjeta = MedioDePago.builder().idMedioDePago(1).nombreMedioDePago("TARJETA_CREDITO").build();
    }

    @Test
    @DisplayName("Debe crear un carrito nuevo si el usuario existe y no tiene uno activo")
    void crearCarritoExitoso() {
        int idUsuario = faker.number().positive();
        String fakeEmail = faker.internet().emailAddress();

        UsuarioExternoDTO usuarioExterno = UsuarioExternoDTO.builder()
                .idUsuario(idUsuario)
                .nombreUsuario(faker.name().username())
                .email(fakeEmail)
                .build();

        CrearCarritoRequestDTO request = CrearCarritoRequestDTO.builder()
                .idUsuario(idUsuario)
                .build();

        Transaccion nuevoCarrito = Transaccion.builder()
                .idTransaccion(100)
                .idUsuario(idUsuario)
                .estado(estadoEnCarrito)
                .total(0.0)
                .items(new ArrayList<>())
                .build();

        when(usuarioClient.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioExterno);
        when(transaccionRepository.findByIdUsuarioAndEstadoNombreEstado(idUsuario, "EN_CARRITO"))
                .thenReturn(Optional.empty());
        when(estadoRepository.findByNombreEstado("EN_CARRITO")).thenReturn(Optional.of(estadoEnCarrito));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(nuevoCarrito);

        Transaccion resultado = transaccionService.obtenerOCrearCarrito(request);

        assertNotNull(resultado);
        assertEquals(100, resultado.getIdTransaccion());
        assertEquals("EN_CARRITO", resultado.getEstado().getNombreEstado());
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(idUsuario);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el usuario no existe en servicio-usuarios")
    void crearCarritoUsuarioNoExistenteLanzaError() {
        int idUsuarioInvalido = 999;
        CrearCarritoRequestDTO request = CrearCarritoRequestDTO.builder().idUsuario(idUsuarioInvalido).build();

        when(usuarioClient.obtenerUsuarioPorId(idUsuarioInvalido)).thenThrow(new ResourceNotFoundException("Usuario no encontrado"));

        assertThrows(ResourceNotFoundException.class, () -> transaccionService.obtenerOCrearCarrito(request));
        verify(transaccionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe agregar un ítem al carrito y recalcular el total correctamente")
    void agregarItemAlCarritoRecalculaTotal() {
        int idTransaccion = 10;
        int idProducto = 5;
        double precioProducto = 2.50;
        int cantidad = 4;

        Transaccion carrito = Transaccion.builder()
                .idTransaccion(idTransaccion)
                .idUsuario(1)
                .estado(estadoEnCarrito)
                .total(0.0)
                .items(new ArrayList<>())
                .build();

        ProductoExternoDTO productoExterno = ProductoExternoDTO.builder()
                .idProducto(idProducto)
                .nombreProducto("Divine Orb")
                .precio(precioProducto)
                .stock(100)
                .build();

        ItemCarritoRequestDTO itemRequest = ItemCarritoRequestDTO.builder()
                .productoId(idProducto)
                .cantidad(cantidad)
                .build();

        when(transaccionRepository.findById(idTransaccion)).thenReturn(Optional.of(carrito));
        when(productoClient.obtenerProductoPorId(idProducto)).thenReturn(productoExterno);
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaccion resultado = transaccionService.agregarOActualizarItem(idTransaccion, itemRequest);

        assertNotNull(resultado);
        assertEquals(1, resultado.getItems().size());
        assertEquals(10.0, resultado.getTotal()); // 2.50 * 4 = 10.0
        verify(productoClient, times(1)).obtenerProductoPorId(idProducto);
    }

    @Test
    @DisplayName("Debe completar el checkout: descontar stock en servicio-productos y cambiar a PAGADO")
    void checkoutExitosoDescuentaStock() {
        int idTransaccion = 20;
        int idProducto = 7;
        int cantidad = 2;

        Transaccion carrito = Transaccion.builder()
                .idTransaccion(idTransaccion)
                .idUsuario(1)
                .estado(estadoEnCarrito)
                .total(50.0)
                .items(new ArrayList<>())
                .build();

        TransaccionProducto item = TransaccionProducto.builder()
                .id(new TransaccionProductoId(idTransaccion, idProducto))
                .transaccion(carrito)
                .precioProducto(25.0)
                .cantidadProducto(cantidad)
                .build();
        carrito.getItems().add(item);

        CheckoutRequestDTO checkoutRequest = CheckoutRequestDTO.builder()
                .idMedioDePago(1)
                .build();

        when(transaccionRepository.findById(idTransaccion)).thenReturn(Optional.of(carrito));
        when(medioDePagoRepository.findById(1)).thenReturn(Optional.of(medioDePagoTarjeta));
        when(estadoRepository.findByNombreEstado("PAGADO")).thenReturn(Optional.of(estadoPagado));
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaccion resultado = transaccionService.checkout(idTransaccion, checkoutRequest);

        assertNotNull(resultado);
        assertEquals("PAGADO", resultado.getEstado().getNombreEstado());
        assertEquals("TARJETA_CREDITO", resultado.getMedioDePago().getNombreMedioDePago());
        verify(productoClient, times(1)).descontarStock(idProducto, cantidad);
    }

    @Test
    @DisplayName("Debe lanzar TransaccionInvalidaException al intentar checkout de un carrito vacío")
    void checkoutCarritoVacioLanzaError() {
        int idTransaccion = 30;
        Transaccion carritoVacio = Transaccion.builder()
                .idTransaccion(idTransaccion)
                .idUsuario(1)
                .estado(estadoEnCarrito)
                .total(0.0)
                .items(new ArrayList<>())
                .build();

        when(transaccionRepository.findById(idTransaccion)).thenReturn(Optional.of(carritoVacio));

        CheckoutRequestDTO checkoutRequest = CheckoutRequestDTO.builder().idMedioDePago(1).build();

        assertThrows(TransaccionInvalidaException.class, () ->
                transaccionService.checkout(idTransaccion, checkoutRequest)
        );
        verify(productoClient, never()).descontarStock(anyInt(), anyInt());
    }
}
