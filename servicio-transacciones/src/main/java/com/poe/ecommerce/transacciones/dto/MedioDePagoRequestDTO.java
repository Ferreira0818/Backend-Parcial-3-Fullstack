package com.poe.ecommerce.transacciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedioDePagoRequestDTO {

    @NotBlank(message = "El nombre del medio de pago es obligatorio")
    @Size(max = 80, message = "El nombre no debe superar los 80 caracteres")
    private String nombreMedioDePago;
}
