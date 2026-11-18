package com.poe.ecommerce.transacciones.service;

import com.poe.ecommerce.transacciones.dto.MedioDePagoDTO;
import com.poe.ecommerce.transacciones.dto.MedioDePagoRequestDTO;
import com.poe.ecommerce.transacciones.model.MedioDePago;

import java.util.List;

public interface MedioDePagoService {
    List<MedioDePago> listarTodos();
    MedioDePago obtenerPorId(Integer id);
    MedioDePagoDTO obtenerDtoPorId(Integer id);
    MedioDePago crear(MedioDePagoRequestDTO request);
    MedioDePago actualizar(Integer id, MedioDePagoRequestDTO request);
    void eliminar(Integer id);
}
