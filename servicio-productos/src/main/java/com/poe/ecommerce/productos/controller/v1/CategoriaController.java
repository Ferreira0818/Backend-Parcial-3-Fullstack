package com.poe.ecommerce.productos.controller.v1;

import com.poe.ecommerce.productos.dto.CategoriaDTO;
import com.poe.ecommerce.productos.dto.CategoriaRequestDTO;
import com.poe.ecommerce.productos.model.Categoria;
import com.poe.ecommerce.productos.service.CategoriaService;
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
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías - API v1", description = "Endpoints REST convencionales para gestión de categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas().stream()
                .map(c -> CategoriaDTO.builder().idCategoria(c.getIdCategoria()).nombreCategoria(c.getNombreCategoria()).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerDtoPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<CategoriaDTO> crear(@Valid @RequestBody CategoriaRequestDTO request) {
        Categoria creada = categoriaService.crear(request);
        return new ResponseEntity<>(
                CategoriaDTO.builder().idCategoria(creada.getIdCategoria()).nombreCategoria(creada.getNombreCategoria()).build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría existente")
    public ResponseEntity<CategoriaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO request) {
        Categoria actualizada = categoriaService.actualizar(id, request);
        return ResponseEntity.ok(
                CategoriaDTO.builder().idCategoria(actualizada.getIdCategoria()).nombreCategoria(actualizada.getNombreCategoria()).build()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
