package com.poe.ecommerce.transacciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoExternoDTO {
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
