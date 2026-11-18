package com.poe.ecommerce.transacciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedioDePagoDTO {
    private Integer idMedioDePago;
    private String nombreMedioDePago;
}
