package com.poe.ecommerce.transacciones.controller.v2;

import com.poe.ecommerce.transacciones.assemblers.EstadoModelAssembler;
import com.poe.ecommerce.transacciones.dto.EstadoDTO;
import com.poe.ecommerce.transacciones.model.Estado;
import com.poe.ecommerce.transacciones.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/estados")
@RequiredArgsConstructor
@Tag(name = "Estados - API v2 (HATEOAS)", description = "Endpoints de estados enriquecidos con hipermedios HATEOAS")
public class EstadoControllerV2 {

    private final EstadoService estadoService;
    private final EstadoModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar estados con hipermedios HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<EstadoDTO>>> listarTodos() {
        List<Estado> estados = estadoService.listarTodos();
        CollectionModel<EntityModel<EstadoDTO>> model = assembler.toCollectionModel(estados);
        model.add(linkTo(methodOn(EstadoControllerV2.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado por ID con hipermedios")
    public ResponseEntity<EntityModel<EstadoDTO>> obtenerPorId(@PathVariable Integer id) {
        Estado estado = estadoService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(estado));
    }
}
