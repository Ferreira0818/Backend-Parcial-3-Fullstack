package com.poe.ecommerce.productos.service;

import com.poe.ecommerce.productos.dto.ProductoRequestDTO;
import com.poe.ecommerce.productos.dto.ProductoResponseDTO;
import com.poe.ecommerce.productos.model.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> listarTodos();
    List<Producto> listarPorCategoria(Integer idCategoria);
    Producto obtenerPorId(Integer id);
    ProductoResponseDTO obtenerDtoPorId(Integer id);
    Producto crear(ProductoRequestDTO request);
    Producto actualizar(Integer id, ProductoRequestDTO request);
    Producto descontarStock(Integer id, int cantidad);
    void eliminar(Integer id);
    ProductoResponseDTO mapearAResponseDTO(Producto producto);
}
