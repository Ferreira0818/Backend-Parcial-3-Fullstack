package com.poe.ecommerce.transacciones.repository;

import com.poe.ecommerce.transacciones.model.MedioDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedioDePagoRepository extends JpaRepository<MedioDePago, Integer> {
    Optional<MedioDePago> findByNombreMedioDePago(String nombreMedioDePago);
    boolean existsByNombreMedioDePago(String nombreMedioDePago);
}
