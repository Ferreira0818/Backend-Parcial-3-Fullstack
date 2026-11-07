package com.poe.ecommerce.transacciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescontarStockDTO {
    private Integer cantidad;
}
