package com.poe.ecommerce.productos.assemblers;

import com.poe.ecommerce.productos.controller.v2.CategoriaControllerV2;
import com.poe.ecommerce.productos.dto.CategoriaDTO;
import com.poe.ecommerce.productos.model.Categoria;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaModelAssembler extends RepresentationModelAssemblerSupport<Categoria, EntityModel<CategoriaDTO>> {

    public CategoriaModelAssembler() {
        super(CategoriaControllerV2.class, (Class<EntityModel<CategoriaDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<CategoriaDTO> toModel(Categoria entity) {
        CategoriaDTO dto = CategoriaDTO.builder()
                .idCategoria(entity.getIdCategoria())
                .nombreCategoria(entity.getNombreCategoria())
                .build();

        return EntityModel.of(dto,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerPorId(entity.getIdCategoria())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).listarTodas()).withRel("categorias")
        );
    }
}
