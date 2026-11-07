package com.poe.ecommerce.usuarios.service;

import com.poe.ecommerce.usuarios.dto.RolDTO;
import com.poe.ecommerce.usuarios.dto.RolRequestDTO;
import com.poe.ecommerce.usuarios.model.Rol;

import java.util.List;

public interface RolService {
    List<Rol> listarTodos();
    Rol obtenerPorId(Integer id);
    RolDTO obtenerDtoPorId(Integer id);
    Rol crear(RolRequestDTO request);
    Rol actualizar(Integer id, RolRequestDTO request);
    void eliminar(Integer id);
}
