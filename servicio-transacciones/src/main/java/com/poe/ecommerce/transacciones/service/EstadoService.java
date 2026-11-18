package com.poe.ecommerce.transacciones.service;

import com.poe.ecommerce.transacciones.dto.EstadoDTO;
import com.poe.ecommerce.transacciones.model.Estado;

import java.util.List;

public interface EstadoService {
    List<Estado> listarTodos();
    Estado obtenerPorId(Integer id);
    Estado obtenerPorNombre(String nombre);
    EstadoDTO obtenerDtoPorId(Integer id);
}
