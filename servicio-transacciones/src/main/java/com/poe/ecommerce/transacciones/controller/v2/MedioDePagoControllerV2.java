package com.poe.ecommerce.transacciones.controller.v2;

import com.poe.ecommerce.transacciones.assemblers.MedioDePagoModelAssembler;
import com.poe.ecommerce.transacciones.dto.MedioDePagoDTO;
import com.poe.ecommerce.transacciones.dto.MedioDePagoRequestDTO;
import com.poe.ecommerce.transacciones.model.MedioDePago;
import com.poe.ecommerce.transacciones.service.MedioDePagoService;
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
@RequestMapping("/api/v2/medios-pago")
@RequiredArgsConstructor
@Tag(name = "Medios de Pago - API v2 (HATEOAS)", description = "Endpoints de medios de pago enriquecidos con hipermedios")
public class MedioDePagoControllerV2 {

    private final MedioDePagoService medioDePagoService;
    private final MedioDePagoModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar medios de pago con hipermedios")
    public ResponseEntity<CollectionModel<EntityModel<MedioDePagoDTO>>> listarTodos() {
        List<MedioDePago> medios = medioDePagoService.listarTodos();
        CollectionModel<EntityModel<MedioDePagoDTO>> model = assembler.toCollectionModel(medios);
        model.add(linkTo(methodOn(MedioDePagoControllerV2.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener medio de pago por ID con hipermedios")
    public ResponseEntity<EntityModel<MedioDePagoDTO>> obtenerPorId(@PathVariable Integer id) {
        MedioDePago medio = medioDePagoService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(medio));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo medio de pago con hipermedios")
    public ResponseEntity<EntityModel<MedioDePagoDTO>> crear(@Valid @RequestBody MedioDePagoRequestDTO request) {
        MedioDePago creado = medioDePagoService.crear(request);
        EntityModel<MedioDePagoDTO> model = assembler.toModel(creado);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar medio de pago con hipermedios")
    public ResponseEntity<EntityModel<MedioDePagoDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody MedioDePagoRequestDTO request) {
        MedioDePago actualizado = medioDePagoService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar medio de pago por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        medioDePagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
