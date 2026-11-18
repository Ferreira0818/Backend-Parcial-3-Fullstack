package com.poe.ecommerce.productos.dto;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "productos", itemRelation = "producto")
public class ProductoResponseDTO {
    private Integer idProducto;
    private String nombreProducto;
    private String imagen;
    private Double precio;
    private String alt;
    private String descripcion;
    private Integer stock;
    private Integer categoriaId;
    private String nombreCategoria;
}
