package com.poe.ecommerce.transacciones.controller.v2;

import com.poe.ecommerce.transacciones.assemblers.TransaccionModelAssembler;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/transacciones")
@RequiredArgsConstructor
@Tag(name = "Transacciones y Carrito - API v2 (HATEOAS)", description = "Endpoints de compras y carritos enriquecidos con hipermedios")
public class TransaccionControllerV2 {

    private final TransaccionService transaccionService;
    private final TransaccionModelAssembler assembler;

    @PostMapping("/carrito")
    @Operation(summary = "Crear o recuperar carrito con hipermedios HATEOAS")
    public ResponseEntity<EntityModel<TransaccionResponseDTO>> obtenerOCrearCarrito(
            @Valid @RequestBody CrearCarritoRequestDTO request) {
        Transaccion carrito = transaccionService.obtenerOCrearCarrito(request);
        EntityModel<TransaccionResponseDTO> model = assembler.toModel(carrito);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Agregar o modificar ítem en el carrito con hipermedios")
    public ResponseEntity<EntityModel<TransaccionResponseDTO>> agregarOActualizarItem(
            @PathVariable Integer id,
            @Valid @RequestBody ItemCarritoRequestDTO itemRequest) {
        Transaccion actualizada = transaccionService.agregarOActualizarItem(id, itemRequest);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping("/{id}/items/{productoId}")
    @Operation(summary = "Quitar producto del carrito con hipermedios")
    public ResponseEntity<EntityModel<TransaccionResponseDTO>> eliminarItem(
            @PathVariable Integer id,
            @PathVariable Integer productoId) {
        Transaccion actualizada = transaccionService.eliminarItem(id, productoId);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @PostMapping("/{id}/checkout")
    @Operation(summary = "Confirmar compra (checkout) con hipermedios")
    public ResponseEntity<EntityModel<TransaccionResponseDTO>> checkout(
            @PathVariable Integer id,
            @Valid @RequestBody(required = false) CheckoutRequestDTO checkoutRequest) {
        Transaccion pagada = transaccionService.checkout(id, checkoutRequest);
        return ResponseEntity.ok(assembler.toModel(pagada));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar transacción con hipermedios")
    public ResponseEntity<EntityModel<TransaccionResponseDTO>> cancelar(@PathVariable Integer id) {
        Transaccion cancelada = transaccionService.cancelarTransaccion(id);
        return ResponseEntity.ok(assembler.toModel(cancelada));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción por ID con hipermedios HATEOAS")
    public ResponseEntity<EntityModel<TransaccionResponseDTO>> obtenerPorId(@PathVariable Integer id) {
        Transaccion transaccion = transaccionService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(transaccion));
    }

    @GetMapping
    @Operation(summary = "Listar transacciones con hipermedios HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<TransaccionResponseDTO>>> listarTodas(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) String estado) {
        List<Transaccion> transacciones = transaccionService.listarTodas(idUsuario, estado);
        CollectionModel<EntityModel<TransaccionResponseDTO>> model = assembler.toCollectionModel(transacciones);
        model.add(linkTo(methodOn(TransaccionControllerV2.class).listarTodas(idUsuario, estado)).withSelfRel());
        return ResponseEntity.ok(model);
    }
}
