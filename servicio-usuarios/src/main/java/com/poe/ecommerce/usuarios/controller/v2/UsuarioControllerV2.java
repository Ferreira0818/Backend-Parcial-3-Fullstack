package com.poe.ecommerce.usuarios.controller.v2;

import com.poe.ecommerce.usuarios.assemblers.UsuarioModelAssembler;
import com.poe.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.poe.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.poe.ecommerce.usuarios.model.Usuario;
import com.poe.ecommerce.usuarios.service.UsuarioService;
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
@RequestMapping("/api/v2/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios - API v2 (HATEOAS)", description = "Endpoints enriquecidos con hipermedios HATEOAS")
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios con hipermedios HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        CollectionModel<EntityModel<UsuarioResponseDTO>> model = assembler.toCollectionModel(usuarios);
        model.add(linkTo(methodOn(UsuarioControllerV2.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID con hipermedios")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(usuario));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario con hipermedios")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> crear(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario creado = usuarioService.crear(request);
        EntityModel<UsuarioResponseDTO> model = assembler.toModel(creado);
        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario existente con hipermedios")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO request) {
        Usuario actualizado = usuarioService.actualizar(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
