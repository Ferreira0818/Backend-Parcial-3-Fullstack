package com.poe.ecommerce.usuarios.controller.v1;

import com.poe.ecommerce.usuarios.dto.RolDTO;
import com.poe.ecommerce.usuarios.dto.RolRequestDTO;
import com.poe.ecommerce.usuarios.model.Rol;
import com.poe.ecommerce.usuarios.service.RolService;
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
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles - API v1", description = "Endpoints REST convencionales para gestión de roles")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @Operation(summary = "Listar todos los roles")
    public ResponseEntity<List<RolDTO>> listarTodos() {
        List<RolDTO> lista = rolService.listarTodos().stream()
                .map(r -> RolDTO.builder().idRol(r.getIdRol()).nombreRol(r.getNombreRol()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID")
    public ResponseEntity<RolDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(rolService.obtenerDtoPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo rol")
    public ResponseEntity<RolDTO> crear(@Valid @RequestBody RolRequestDTO request) {
        Rol creado = rolService.crear(request);
        return new ResponseEntity<>(
                RolDTO.builder().idRol(creado.getIdRol()).nombreRol(creado.getNombreRol()).build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol existente")
    public ResponseEntity<RolDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody RolRequestDTO request) {
        Rol actualizado = rolService.actualizar(id, request);
        return ResponseEntity.ok(
                RolDTO.builder().idRol(actualizado.getIdRol()).nombreRol(actualizado.getNombreRol()).build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
