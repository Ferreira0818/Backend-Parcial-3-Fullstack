package com.poe.ecommerce.transacciones.controller.v1;

import com.poe.ecommerce.transacciones.dto.EstadoDTO;
import com.poe.ecommerce.transacciones.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/estados")
@RequiredArgsConstructor
@Tag(name = "Estados - API v1", description = "Endpoints REST para consulta de estados de transacción")
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    @Operation(summary = "Listar todos los estados de transacción")
    public ResponseEntity<List<EstadoDTO>> listarTodos() {
        List<EstadoDTO> estados = estadoService.listarTodos().stream()
                .map(e -> EstadoDTO.builder().idEstado(e.getIdEstado()).nombreEstado(e.getNombreEstado()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID")
    public ResponseEntity<EstadoDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoService.obtenerDtoPorId(id));
    }
}
