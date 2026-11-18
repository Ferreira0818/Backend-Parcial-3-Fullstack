package com.poe.ecommerce.productos.controller.v1;

import com.poe.ecommerce.productos.dto.DescontarStockDTO;
import com.poe.ecommerce.productos.dto.ProductoRequestDTO;
import com.poe.ecommerce.productos.dto.ProductoResponseDTO;
import com.poe.ecommerce.productos.model.Producto;
import com.poe.ecommerce.productos.service.ProductoService;
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
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Productos - API v1", description = "Endpoints REST convencionales para catálogo de productos")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar productos (con filtro opcional por categoría)")
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos(
            @RequestParam(required = false) Integer categoriaId) {
        List<Producto> productos = (categoriaId != null)
                ? productoService.listarPorCategoria(categoriaId)
                : productoService.listarTodos();

        List<ProductoResponseDTO> response = productos.stream()
                .map(productoService::mapearAResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID (consumido por servicio-transacciones)")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerDtoPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO request) {
        Producto creado = productoService.crear(request);
        return new ResponseEntity<>(productoService.mapearAResponseDTO(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDTO request) {
        Producto actualizado = productoService.actualizar(id, request);
        return ResponseEntity.ok(productoService.mapearAResponseDTO(actualizado));
    }

    @PatchMapping("/{id}/descontar-stock")
    @Operation(summary = "Descontar existencias de stock (consumido por checkout en servicio-transacciones)")
    public ResponseEntity<ProductoResponseDTO> descontarStock(
            @PathVariable Integer id,
            @Valid @RequestBody DescontarStockDTO request) {
        Producto producto = productoService.descontarStock(id, request.getCantidad());
        return ResponseEntity.ok(productoService.mapearAResponseDTO(producto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
