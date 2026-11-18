package com.poe.ecommerce.transacciones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionProductoId implements Serializable {

    @Column(name = "TransaccionId")
    private Integer transaccionId;

    @Column(name = "ProductoId")
    private Integer productoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransaccionProductoId that = (TransaccionProductoId) o;
        return Objects.equals(transaccionId, that.transaccionId) &&
               Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaccionId, productoId);
    }
}
