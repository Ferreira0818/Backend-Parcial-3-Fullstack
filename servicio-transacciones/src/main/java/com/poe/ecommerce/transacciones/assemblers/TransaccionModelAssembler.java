package com.poe.ecommerce.transacciones.assemblers;

import com.poe.ecommerce.transacciones.controller.v2.TransaccionControllerV2;
import com.poe.ecommerce.transacciones.dto.TransaccionResponseDTO;
import com.poe.ecommerce.transacciones.model.Transaccion;
import com.poe.ecommerce.transacciones.service.TransaccionService;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransaccionModelAssembler extends RepresentationModelAssemblerSupport<Transaccion, EntityModel<TransaccionResponseDTO>> {

    private final TransaccionService transaccionService;

    public TransaccionModelAssembler(@Lazy TransaccionService transaccionService) {
        super(TransaccionControllerV2.class, (Class<EntityModel<TransaccionResponseDTO>>) (Class<?>) EntityModel.class);
        this.transaccionService = transaccionService;
    }

    @Override
    public EntityModel<TransaccionResponseDTO> toModel(Transaccion entity) {
        TransaccionResponseDTO dto = transaccionService.mapearAResponseDTO(entity);

        EntityModel<TransaccionResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(TransaccionControllerV2.class).obtenerPorId(entity.getIdTransaccion())).withSelfRel(),
                linkTo(methodOn(TransaccionControllerV2.class).listarTodas(null, null)).withRel("transacciones")
        );

        if ("EN_CARRITO".equalsIgnoreCase(dto.getEstado())) {
            model.add(linkTo(methodOn(TransaccionControllerV2.class).checkout(entity.getIdTransaccion(), null)).withRel("checkout"));
            model.add(linkTo(methodOn(TransaccionControllerV2.class).cancelar(entity.getIdTransaccion())).withRel("cancelar"));
        }

        return model;
    }
}
