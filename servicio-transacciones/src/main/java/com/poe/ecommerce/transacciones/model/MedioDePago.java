package com.poe.ecommerce.transacciones.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MedioDePago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMedioDePago")
    private Integer idMedioDePago;

    @Column(name = "NombreMedioDePago", nullable = false, unique = true, length = 80)
    private String nombreMedioDePago;
}
