package com.poe.ecommerce.transacciones.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEstado")
    private Integer idEstado;

    @Column(name = "NombreEstado", nullable = false, unique = true, length = 50)
    private String nombreEstado;
}
