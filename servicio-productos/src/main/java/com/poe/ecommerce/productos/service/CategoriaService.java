package com.poe.ecommerce.productos.service;

import com.poe.ecommerce.productos.dto.CategoriaDTO;
import com.poe.ecommerce.productos.dto.CategoriaRequestDTO;
import com.poe.ecommerce.productos.model.Categoria;

import java.util.List;

public interface CategoriaService {
    List<Categoria> listarTodas();
    Categoria obtenerPorId(Integer id);
    CategoriaDTO obtenerDtoPorId(Integer id);
    Categoria crear(CategoriaRequestDTO request);
    Categoria actualizar(Integer id, CategoriaRequestDTO request);
    void eliminar(Integer id);
}
