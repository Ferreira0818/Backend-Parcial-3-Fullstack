package com.poe.ecommerce.transacciones.controller.v1;

import com.poe.ecommerce.transacciones.dto.MedioDePagoDTO;
import com.poe.ecommerce.transacciones.dto.MedioDePagoRequestDTO;
import com.poe.ecommerce.transacciones.model.MedioDePago;
import com.poe.ecommerce.transacciones.service.MedioDePagoService;
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
@RequestMapping("/api/v1/medios-pago")
@RequiredArgsConstructor
@Tag(name = "Medios de Pago - API v1", description = "Endpoints REST para gestión de medios de pago")
public class MedioDePagoController {

    private final MedioDePagoService medioDePagoService;

    @GetMapping
    @Operation(summary = "Listar todos los medios de pago disponibles")
    public ResponseEntity<List<MedioDePagoDTO>> listarTodos() {
        List<MedioDePagoDTO> lista = medioDePagoService.listarTodos().stream()
                .map(m -> MedioDePagoDTO.builder().idMedioDePago(m.getIdMedioDePago()).nombreMedioDePago(m.getNombreMedioDePago()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener medio de pago por ID")
    public ResponseEntity<MedioDePagoDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(medioDePagoService.obtenerDtoPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo medio de pago")
    public ResponseEntity<MedioDePagoDTO> crear(@Valid @RequestBody MedioDePagoRequestDTO request) {
        MedioDePago creado = medioDePagoService.crear(request);
        return new ResponseEntity<>(
                MedioDePagoDTO.builder().idMedioDePago(creado.getIdMedioDePago()).nombreMedioDePago(creado.getNombreMedioDePago()).build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar medio de pago existente")
    public ResponseEntity<MedioDePagoDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody MedioDePagoRequestDTO request) {
        MedioDePago actualizado = medioDePagoService.actualizar(id, request);
        return ResponseEntity.ok(
                MedioDePagoDTO.builder().idMedioDePago(actualizado.getIdMedioDePago()).nombreMedioDePago(actualizado.getNombreMedioDePago()).build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar medio de pago por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        medioDePagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
