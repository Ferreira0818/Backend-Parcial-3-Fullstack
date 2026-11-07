package com.poe.ecommerce.usuarios.controller.v2;

import com.poe.ecommerce.usuarios.assemblers.RolModelAssembler;
import com.poe.ecommerce.usuarios.dto.RolDTO;
import com.poe.ecommerce.usuarios.dto.RolRequestDTO;
import com.poe.ecommerce.usuarios.model.Rol;
import com.poe.ecommerce.usuarios.service.RolService;
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
@RequestMapping("/api/v2/roles")
@RequiredArgsConstructor
@Tag(name = "Roles - API v2 (HATEOAS)", description = "Endpoints enriquecidos con hipermedios HATEOAS")
public class RolControllerV2 {

    private final RolService rolService;
    private final RolModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todos los roles con hipermedios")
    public ResponseEntity<CollectionModel<EntityModel<RolDTO>>> listarTodos() {
        List<Rol> roles = rolService.listarTodos();
        CollectionModel<EntityModel<RolDTO>> model = assembler.toCollectionModel(roles);
        model.add(linkTo(methodOn(RolControllerV2.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID con hipermedios")
    public ResponseEntity<EntityModel<RolDTO>> obtenerPorId(@PathVariable Integer id) {
        Rol rol = rolService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(rol));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo rol con hipermedios")
    public ResponseEntity<EntityModel<RolDTO>> crear(@Valid @RequestBody RolRequestDTO request) {
        Rol creado = rolService.crear(request);
        EntityModel<RolDTO> model = assembler.toModel(creado);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol existente con hipermedios")
    public ResponseEntity<EntityModel<RolDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody RolRequestDTO request) {
        Rol actualizado = rolService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
