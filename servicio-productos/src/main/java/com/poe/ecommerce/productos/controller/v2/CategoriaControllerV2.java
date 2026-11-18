package com.poe.ecommerce.productos.controller.v2;

import com.poe.ecommerce.productos.assemblers.CategoriaModelAssembler;
import com.poe.ecommerce.productos.dto.CategoriaDTO;
import com.poe.ecommerce.productos.dto.CategoriaRequestDTO;
import com.poe.ecommerce.productos.model.Categoria;
import com.poe.ecommerce.productos.service.CategoriaService;
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
@RequestMapping("/api/v2/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías - API v2 (HATEOAS)", description = "Endpoints enriquecidos con hipermedios HATEOAS")
public class CategoriaControllerV2 {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todas las categorías con hipermedios")
    public ResponseEntity<CollectionModel<EntityModel<CategoriaDTO>>> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        CollectionModel<EntityModel<CategoriaDTO>> model = assembler.toCollectionModel(categorias);
        model.add(linkTo(methodOn(CategoriaControllerV2.class).listarTodas()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID con hipermedios")
    public ResponseEntity<EntityModel<CategoriaDTO>> obtenerPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría con hipermedios")
    public ResponseEntity<EntityModel<CategoriaDTO>> crear(@Valid @RequestBody CategoriaRequestDTO request) {
        Categoria creada = categoriaService.crear(request);
        EntityModel<CategoriaDTO> model = assembler.toModel(creada);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría existente con hipermedios")
    public ResponseEntity<EntityModel<CategoriaDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO request) {
        Categoria actualizada = categoriaService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
