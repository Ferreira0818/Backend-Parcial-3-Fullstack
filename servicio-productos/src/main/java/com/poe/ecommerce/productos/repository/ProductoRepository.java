package com.poe.ecommerce.productos.repository;

import com.poe.ecommerce.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombreProducto(String nombreProducto);
    boolean existsByNombreProducto(String nombreProducto);
    List<Producto> findByCategoriaIdCategoria(Integer idCategoria);
}
