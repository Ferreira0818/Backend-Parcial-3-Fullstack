package com.poe.ecommerce.usuarios.service.impl;

import com.poe.ecommerce.usuarios.dto.RolDTO;
import com.poe.ecommerce.usuarios.dto.RolRequestDTO;
import com.poe.ecommerce.usuarios.exception.BadRequestException;
import com.poe.ecommerce.usuarios.exception.ResourceNotFoundException;
import com.poe.ecommerce.usuarios.model.Rol;
import com.poe.ecommerce.usuarios.repository.RolRepository;
import com.poe.ecommerce.usuarios.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol obtenerPorId(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol con id " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public RolDTO obtenerDtoPorId(Integer id) {
        Rol rol = obtenerPorId(id);
        return RolDTO.builder()
                .idRol(rol.getIdRol())
                .nombreRol(rol.getNombreRol())
                .build();
    }

    @Override
    @Transactional
    public Rol crear(RolRequestDTO request) {
        if (rolRepository.existsByNombreRol(request.getNombreRol())) {
            throw new BadRequestException("Ya existe un rol con el nombre: " + request.getNombreRol());
        }
        Rol rol = Rol.builder()
                .nombreRol(request.getNombreRol().trim().toUpperCase())
                .build();
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizar(Integer id, RolRequestDTO request) {
        Rol rol = obtenerPorId(id);
        String nuevoNombre = request.getNombreRol().trim().toUpperCase();
        if (!rol.getNombreRol().equalsIgnoreCase(nuevoNombre) && rolRepository.existsByNombreRol(nuevoNombre)) {
            throw new BadRequestException("Ya existe otro rol con el nombre: " + nuevoNombre);
        }
        rol.setNombreRol(nuevoNombre);
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Rol rol = obtenerPorId(id);
        rolRepository.delete(rol);
    }
}
