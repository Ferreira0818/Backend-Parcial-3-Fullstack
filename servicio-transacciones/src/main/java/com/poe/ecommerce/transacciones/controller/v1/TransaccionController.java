package com.poe.ecommerce.transacciones.controller.v1;

import com.poe.ecommerce.transacciones.dto.CheckoutRequestDTO;
import com.poe.ecommerce.transacciones.dto.CrearCarritoRequestDTO;
import com.poe.ecommerce.transacciones.dto.ItemCarritoRequestDTO;
import com.poe.ecommerce.transacciones.dto.TransaccionResponseDTO;
import com.poe.ecommerce.transacciones.model.Transaccion;
import com.poe.ecommerce.transacciones.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
@Tag(name = "Transacciones y Carrito - API v1", description = "Endpoints REST para el ciclo de vida de carritos, órdenes y compras")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping("/carrito")
    @Operation(summary = "Crear o recuperar el carrito activo de un usuario (valida existencia de usuario con servicio-usuarios)")
    public ResponseEntity<TransaccionResponseDTO> obtenerOCrearCarrito(@Valid @RequestBody CrearCarritoRequestDTO request) {
        Transaccion carrito = transaccionService.obtenerOCrearCarrito(request);
        return new ResponseEntity<>(transaccionService.mapearAResponseDTO(carrito), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Agregar o modificar un ítem en el carrito (valida producto y stock con servicio-productos)")
    public ResponseEntity<TransaccionResponseDTO> agregarOActualizarItem(
            @PathVariable Integer id,
            @Valid @RequestBody ItemCarritoRequestDTO itemRequest) {
        Transaccion actualizada = transaccionService.agregarOActualizarItem(id, itemRequest);
        return ResponseEntity.ok(transaccionService.mapearAResponseDTO(actualizada));
    }

    @DeleteMapping("/{id}/items/{productoId}")
    @Operation(summary = "Quitar un producto del carrito y recalcular total")
    public ResponseEntity<TransaccionResponseDTO> eliminarItem(
            @PathVariable Integer id,
            @PathVariable Integer productoId) {
        Transaccion actualizada = transaccionService.eliminarItem(id, productoId);
        return ResponseEntity.ok(transaccionService.mapearAResponseDTO(actualizada));
    }

    @PostMapping("/{id}/checkout")
    @Operation(summary = "Confirmar compra (checkout): Asigna medio de pago, descuenta stock real en servicio-productos y cambia estado a PAGADO")
    public ResponseEntity<TransaccionResponseDTO> checkout(
            @PathVariable Integer id,
            @Valid @RequestBody CheckoutRequestDTO checkoutRequest) {
        Transaccion pagada = transaccionService.checkout(id, checkoutRequest);
        return ResponseEntity.ok(transaccionService.mapearAResponseDTO(pagada));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una transacción o carrito activo")
    public ResponseEntity<TransaccionResponseDTO> cancelar(@PathVariable Integer id) {
        Transaccion cancelada = transaccionService.cancelarTransaccion(id);
        return ResponseEntity.ok(transaccionService.mapearAResponseDTO(cancelada));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle completo de una transacción por ID")
    public ResponseEntity<TransaccionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(transaccionService.obtenerDtoPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las transacciones (filtros opcionales: idUsuario, estado)")
    public ResponseEntity<List<TransaccionResponseDTO>> listarTodas(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) String estado) {
        List<TransaccionResponseDTO> transacciones = transaccionService.listarTodas(idUsuario, estado).stream()
                .map(transaccionService::mapearAResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transacciones);
    }
}
