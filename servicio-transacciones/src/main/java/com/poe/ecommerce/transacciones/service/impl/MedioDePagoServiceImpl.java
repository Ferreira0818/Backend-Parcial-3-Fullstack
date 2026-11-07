package com.poe.ecommerce.transacciones.service.impl;

import com.poe.ecommerce.transacciones.dto.MedioDePagoDTO;
import com.poe.ecommerce.transacciones.dto.MedioDePagoRequestDTO;
import com.poe.ecommerce.transacciones.exception.BadRequestException;
import com.poe.ecommerce.transacciones.exception.ResourceNotFoundException;
import com.poe.ecommerce.transacciones.model.MedioDePago;
import com.poe.ecommerce.transacciones.repository.MedioDePagoRepository;
import com.poe.ecommerce.transacciones.service.MedioDePagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedioDePagoServiceImpl implements MedioDePagoService {

    private final MedioDePagoRepository medioDePagoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MedioDePago> listarTodos() {
        return medioDePagoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MedioDePago obtenerPorId(Integer id) {
        return medioDePagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medio de pago con id " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public MedioDePagoDTO obtenerDtoPorId(Integer id) {
        MedioDePago medio = obtenerPorId(id);
        return MedioDePagoDTO.builder()
                .idMedioDePago(medio.getIdMedioDePago())
                .nombreMedioDePago(medio.getNombreMedioDePago())
                .build();
    }

    @Override
    @Transactional
    public MedioDePago crear(MedioDePagoRequestDTO request) {
        String nombre = request.getNombreMedioDePago().trim().toUpperCase();
        if (medioDePagoRepository.existsByNombreMedioDePago(nombre)) {
            throw new BadRequestException("Ya existe un medio de pago con el nombre: " + nombre);
        }
        MedioDePago medio = MedioDePago.builder()
                .nombreMedioDePago(nombre)
                .build();
        return medioDePagoRepository.save(medio);
    }

    @Override
    @Transactional
    public MedioDePago actualizar(Integer id, MedioDePagoRequestDTO request) {
        MedioDePago medio = obtenerPorId(id);
        String nuevoNombre = request.getNombreMedioDePago().trim().toUpperCase();
        if (!medio.getNombreMedioDePago().equalsIgnoreCase(nuevoNombre) &&
                medioDePagoRepository.existsByNombreMedioDePago(nuevoNombre)) {
            throw new BadRequestException("Ya existe otro medio de pago con el nombre: " + nuevoNombre);
        }
        medio.setNombreMedioDePago(nuevoNombre);
        return medioDePagoRepository.save(medio);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        MedioDePago medio = obtenerPorId(id);
        medioDePagoRepository.delete(medio);
    }
}
