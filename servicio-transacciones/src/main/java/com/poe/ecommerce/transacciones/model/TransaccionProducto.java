package com.poe.ecommerce.transacciones.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TransaccionProducto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionProducto {

    @EmbeddedId
    private TransaccionProductoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("transaccionId")
    @JoinColumn(name = "TransaccionId", nullable = false)
    private Transaccion transaccion;

    @Column(name = "PrecioProducto", nullable = false)
    private Double precioProducto;

    @Column(name = "CantidadProducto", nullable = false)
    private Integer cantidadProducto;
}
