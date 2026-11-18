package com.poe.ecommerce.usuarios.dto;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "usuarios", itemRelation = "usuario")
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private String email;
    private LocalDateTime fechaRegistro;
    private Integer idRol;
    private String nombreRol;
}
