package com.poe.ecommerce.transacciones.repository;

import com.poe.ecommerce.transacciones.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    List<Transaccion> findByIdUsuario(Integer idUsuario);
    List<Transaccion> findByEstadoNombreEstado(String nombreEstado);
    Optional<Transaccion> findByIdUsuarioAndEstadoNombreEstado(Integer idUsuario, String nombreEstado);
}
