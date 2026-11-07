package com.poe.ecommerce.productos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProducto")
    private Integer idProducto;

    @Column(name = "Imagen", length = 255)
    private String imagen;

    @Column(name = "Precio", nullable = false)
    private Double precio;

    @Column(name = "NombreProducto", nullable = false, unique = true, length = 120)
    private String nombreProducto;

    @Column(name = "Alt", length = 150)
    private String alt;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @Column(name = "Stock", nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CategoriaId", nullable = false)
    private Categoria categoria;
}
