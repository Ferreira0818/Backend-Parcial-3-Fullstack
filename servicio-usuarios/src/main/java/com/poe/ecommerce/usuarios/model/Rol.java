package com.poe.ecommerce.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRol")
    private Integer idRol;

    @Column(name = "NombreRol", nullable = false, unique = true, length = 50)
    private String nombreRol;
}
