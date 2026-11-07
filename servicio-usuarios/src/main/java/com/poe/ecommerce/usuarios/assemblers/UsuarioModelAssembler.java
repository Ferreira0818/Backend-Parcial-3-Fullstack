package com.poe.ecommerce.usuarios.assemblers;

import com.poe.ecommerce.usuarios.controller.v2.RolControllerV2;
import com.poe.ecommerce.usuarios.controller.v2.UsuarioControllerV2;
import com.poe.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.poe.ecommerce.usuarios.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, EntityModel<UsuarioResponseDTO>> {

    public UsuarioModelAssembler() {
        super(UsuarioControllerV2.class, (Class<EntityModel<UsuarioResponseDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(Usuario entity) {
        UsuarioResponseDTO dto = UsuarioResponseDTO.builder()
                .idUsuario(entity.getIdUsuario())
                .nombreUsuario(entity.getNombreUsuario())
                .email(entity.getEmail())
                .fechaRegistro(entity.getFechaRegistro())
                .idRol(entity.getRol() != null ? entity.getRol().getIdRol() : null)
                .nombreRol(entity.getRol() != null ? entity.getRol().getNombreRol() : null)
                .build();

        EntityModel<UsuarioResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(entity.getIdUsuario())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).listarTodos()).withRel("usuarios")
        );

        if (entity.getRol() != null) {
            model.add(linkTo(methodOn(RolControllerV2.class).obtenerPorId(entity.getRol().getIdRol())).withRel("rol"));
        }

        return model;
    }
}
