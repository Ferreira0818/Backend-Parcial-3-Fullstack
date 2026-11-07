package com.poe.ecommerce.transacciones.repository;

import com.poe.ecommerce.transacciones.model.TransaccionProducto;
import com.poe.ecommerce.transacciones.model.TransaccionProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionProductoRepository extends JpaRepository<TransaccionProducto, TransaccionProductoId> {
    List<TransaccionProducto> findByIdTransaccionId(Integer transaccionId);
}
