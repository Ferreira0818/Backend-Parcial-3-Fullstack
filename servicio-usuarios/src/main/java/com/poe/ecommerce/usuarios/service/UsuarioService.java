package com.poe.ecommerce.usuarios.service;

import com.poe.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.poe.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.poe.ecommerce.usuarios.model.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Usuario obtenerPorId(Integer id);
    UsuarioResponseDTO obtenerDtoPorId(Integer id);
    Usuario crear(UsuarioRequestDTO request);
    Usuario actualizar(Integer id, UsuarioRequestDTO request);
    void eliminar(Integer id);
    UsuarioResponseDTO mapearAResponseDTO(Usuario usuario);
}
