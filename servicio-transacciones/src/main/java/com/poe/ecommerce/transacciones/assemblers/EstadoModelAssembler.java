package com.poe.ecommerce.transacciones.assemblers;

import com.poe.ecommerce.transacciones.controller.v2.EstadoControllerV2;
import com.poe.ecommerce.transacciones.dto.EstadoDTO;
import com.poe.ecommerce.transacciones.model.Estado;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EstadoModelAssembler extends RepresentationModelAssemblerSupport<Estado, EntityModel<EstadoDTO>> {

    public EstadoModelAssembler() {
        super(EstadoControllerV2.class, (Class<EntityModel<EstadoDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<EstadoDTO> toModel(Estado entity) {
        EstadoDTO dto = EstadoDTO.builder()
                .idEstado(entity.getIdEstado())
                .nombreEstado(entity.getNombreEstado())
                .build();

        return EntityModel.of(dto,
                linkTo(methodOn(EstadoControllerV2.class).obtenerPorId(entity.getIdEstado())).withSelfRel(),
                linkTo(methodOn(EstadoControllerV2.class).listarTodos()).withRel("estados")
        );
    }
}
