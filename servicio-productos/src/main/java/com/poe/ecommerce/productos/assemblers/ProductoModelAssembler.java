package com.poe.ecommerce.productos.assemblers;

import com.poe.ecommerce.productos.controller.v2.CategoriaControllerV2;
import com.poe.ecommerce.productos.controller.v2.ProductoControllerV2;
import com.poe.ecommerce.productos.dto.ProductoResponseDTO;
import com.poe.ecommerce.productos.model.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler extends RepresentationModelAssemblerSupport<Producto, EntityModel<ProductoResponseDTO>> {

    public ProductoModelAssembler() {
        super(ProductoControllerV2.class, (Class<EntityModel<ProductoResponseDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<ProductoResponseDTO> toModel(Producto entity) {
        ProductoResponseDTO dto = ProductoResponseDTO.builder()
                .idProducto(entity.getIdProducto())
                .nombreProducto(entity.getNombreProducto())
                .imagen(entity.getImagen())
                .precio(entity.getPrecio())
                .alt(entity.getAlt())
                .descripcion(entity.getDescripcion())
                .stock(entity.getStock())
                .categoriaId(entity.getCategoria() != null ? entity.getCategoria().getIdCategoria() : null)
                .nombreCategoria(entity.getCategoria() != null ? entity.getCategoria().getNombreCategoria() : null)
                .build();

        EntityModel<ProductoResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(ProductoControllerV2.class).obtenerPorId(entity.getIdProducto())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).listarTodos(null)).withRel("productos")
        );

        if (entity.getCategoria() != null) {
            model.add(linkTo(methodOn(CategoriaControllerV2.class).obtenerPorId(entity.getCategoria().getIdCategoria())).withRel("categoria"));
        }

        return model;
    }
}
