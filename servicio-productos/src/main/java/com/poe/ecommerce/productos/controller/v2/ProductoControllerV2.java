package com.poe.ecommerce.productos.controller.v2;

import com.poe.ecommerce.productos.assemblers.ProductoModelAssembler;
import com.poe.ecommerce.productos.dto.ProductoRequestDTO;
import com.poe.ecommerce.productos.dto.ProductoResponseDTO;
import com.poe.ecommerce.productos.model.Producto;
import com.poe.ecommerce.productos.service.ProductoService;
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
@RequestMapping("/api/v2/productos")
@RequiredArgsConstructor
@Tag(name = "Productos - API v2 (HATEOAS)", description = "Endpoints enriquecidos con hipermedios HATEOAS")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar productos con hipermedios (filtro opcional por categoría)")
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> listarTodos(
            @RequestParam(required = false) Integer categoriaId) {
        List<Producto> productos = (categoriaId != null)
                ? productoService.listarPorCategoria(categoriaId)
                : productoService.listarTodos();

        CollectionModel<EntityModel<ProductoResponseDTO>> model = assembler.toCollectionModel(productos);
        model.add(linkTo(methodOn(ProductoControllerV2.class).listarTodos(categoriaId)).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID con hipermedios")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto con hipermedios")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> crear(@Valid @RequestBody ProductoRequestDTO request) {
        Producto creado = productoService.crear(request);
        EntityModel<ProductoResponseDTO> model = assembler.toModel(creado);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente con hipermedios")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDTO request) {
        Producto actualizado = productoService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
