package com.poe.ecommerce.transacciones.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioExternoDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private String email;
    private LocalDateTime fechaRegistro;
    private Integer idRol;
    private String nombreRol;
}
