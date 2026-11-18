package com.poe.ecommerce.transacciones.assemblers;

import com.poe.ecommerce.transacciones.controller.v2.MedioDePagoControllerV2;
import com.poe.ecommerce.transacciones.dto.MedioDePagoDTO;
import com.poe.ecommerce.transacciones.model.MedioDePago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MedioDePagoModelAssembler extends RepresentationModelAssemblerSupport<MedioDePago, EntityModel<MedioDePagoDTO>> {

    public MedioDePagoModelAssembler() {
        super(MedioDePagoControllerV2.class, (Class<EntityModel<MedioDePagoDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<MedioDePagoDTO> toModel(MedioDePago entity) {
        MedioDePagoDTO dto = MedioDePagoDTO.builder()
                .idMedioDePago(entity.getIdMedioDePago())
                .nombreMedioDePago(entity.getNombreMedioDePago())
                .build();

        return EntityModel.of(dto,
                linkTo(methodOn(MedioDePagoControllerV2.class).obtenerPorId(entity.getIdMedioDePago())).withSelfRel(),
                linkTo(methodOn(MedioDePagoControllerV2.class).listarTodos()).withRel("medios-pago")
        );
    }
}
