package com.poe.ecommerce.productos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCategoria")
    private Integer idCategoria;

    @Column(name = "NombreCategoria", nullable = false, unique = true, length = 80)
    private String nombreCategoria;
}
