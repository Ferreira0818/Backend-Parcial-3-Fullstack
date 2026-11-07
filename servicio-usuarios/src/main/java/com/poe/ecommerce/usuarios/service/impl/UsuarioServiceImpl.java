package com.poe.ecommerce.usuarios.service.impl;

import com.poe.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.poe.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.poe.ecommerce.usuarios.exception.BadRequestException;
import com.poe.ecommerce.usuarios.exception.ResourceNotFoundException;
import com.poe.ecommerce.usuarios.model.Rol;
import com.poe.ecommerce.usuarios.model.Usuario;
import com.poe.ecommerce.usuarios.repository.RolRepository;
import com.poe.ecommerce.usuarios.repository.UsuarioRepository;
import com.poe.ecommerce.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con id " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerDtoPorId(Integer id) {
        return mapearAResponseDTO(obtenerPorId(id));
    }

    @Override
    @Transactional
    public Usuario crear(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con el email: " + request.getEmail());
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol con id " + request.getIdRol() + " no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombreUsuario(request.getNombreUsuario().trim())
                .contrasena(request.getContrasena()) // En producción se aplicaría BCryptPasswordEncoder
                .email(request.getEmail().trim().toLowerCase())
                .rol(rol)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizar(Integer id, UsuarioRequestDTO request) {
        Usuario usuario = obtenerPorId(id);

        String nuevoEmail = request.getEmail().trim().toLowerCase();
        if (!usuario.getEmail().equalsIgnoreCase(nuevoEmail) && usuarioRepository.existsByEmail(nuevoEmail)) {
            throw new BadRequestException("Ya existe otro usuario con el email: " + nuevoEmail);
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol con id " + request.getIdRol() + " no encontrado"));

        usuario.setNombreUsuario(request.getNombreUsuario().trim());
        usuario.setContrasena(request.getContrasena());
        usuario.setEmail(nuevoEmail);
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .fechaRegistro(usuario.getFechaRegistro())
                .idRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null)
                .nombreRol(usuario.getRol() != null ? usuario.getRol().getNombreRol() : null)
                .build();
    }
}
