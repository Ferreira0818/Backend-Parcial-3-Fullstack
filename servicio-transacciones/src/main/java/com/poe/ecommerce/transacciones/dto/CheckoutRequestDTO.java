package com.poe.ecommerce.transacciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDTO {

    @NotNull(message = "El ID del medio de pago es obligatorio para realizar el checkout")
    private Integer idMedioDePago;
}
