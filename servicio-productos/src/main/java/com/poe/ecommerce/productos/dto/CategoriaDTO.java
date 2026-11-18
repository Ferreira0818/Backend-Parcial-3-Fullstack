package com.poe.ecommerce.productos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Integer idCategoria;
    private String nombreCategoria;
}
