package com.poe.ecommerce.usuarios.assemblers;

import com.poe.ecommerce.usuarios.controller.v2.RolControllerV2;
import com.poe.ecommerce.usuarios.dto.RolDTO;
import com.poe.ecommerce.usuarios.model.Rol;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RolModelAssembler extends RepresentationModelAssemblerSupport<Rol, EntityModel<RolDTO>> {

    public RolModelAssembler() {
        super(RolControllerV2.class, (Class<EntityModel<RolDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<RolDTO> toModel(Rol entity) {
        RolDTO dto = RolDTO.builder()
                .idRol(entity.getIdRol())
                .nombreRol(entity.getNombreRol())
                .build();

        return EntityModel.of(dto,
                linkTo(methodOn(RolControllerV2.class).obtenerPorId(entity.getIdRol())).withSelfRel(),
                linkTo(methodOn(RolControllerV2.class).listarTodos()).withRel("roles")
        );
    }
}
