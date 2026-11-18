package com.poe.ecommerce.transacciones.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Transaccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTransaccion")
    private Integer idTransaccion;

    @Column(name = "IdUsuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "Total", nullable = false)
    @Builder.Default
    private Double total = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdEstado", nullable = false)
    private Estado estado;

    @CreationTimestamp
    @Column(name = "FechaTransaccion", nullable = false, updatable = false)
    private LocalDateTime fechaTransaccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdMedioDePago")
    private MedioDePago medioDePago;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TransaccionProducto> items = new ArrayList<>();
}
