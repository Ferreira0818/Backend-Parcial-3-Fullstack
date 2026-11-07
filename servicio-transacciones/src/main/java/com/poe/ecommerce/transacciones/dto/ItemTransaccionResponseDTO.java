package com.poe.ecommerce.transacciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTransaccionResponseDTO {
    private Integer productoId;
    private String nombreProducto;
    private Double precioProducto;
    private Integer cantidadProducto;
    private Double subtotal;
}
