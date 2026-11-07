package com.poe.ecommerce.transacciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearCarritoRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    private List<ItemCarritoRequestDTO> items;
}
