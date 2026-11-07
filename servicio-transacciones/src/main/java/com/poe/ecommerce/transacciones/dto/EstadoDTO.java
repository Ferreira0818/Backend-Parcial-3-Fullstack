package com.poe.ecommerce.transacciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoDTO {
    private Integer idEstado;
    private String nombreEstado;
}
