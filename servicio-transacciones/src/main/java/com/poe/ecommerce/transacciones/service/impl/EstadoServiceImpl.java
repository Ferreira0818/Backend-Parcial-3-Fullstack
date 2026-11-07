package com.poe.ecommerce.transacciones.service.impl;

import com.poe.ecommerce.transacciones.dto.EstadoDTO;
import com.poe.ecommerce.transacciones.exception.ResourceNotFoundException;
import com.poe.ecommerce.transacciones.model.Estado;
import com.poe.ecommerce.transacciones.repository.EstadoRepository;
import com.poe.ecommerce.transacciones.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Estado> listarTodos() {
        return estadoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Estado obtenerPorId(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado con id " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Estado obtenerPorNombre(String nombre) {
        return estadoRepository.findByNombreEstado(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Estado con nombre '" + nombre + "' no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoDTO obtenerDtoPorId(Integer id) {
        Estado estado = obtenerPorId(id);
        return EstadoDTO.builder()
                .idEstado(estado.getIdEstado())
                .nombreEstado(estado.getNombreEstado())
                .build();
    }
}
