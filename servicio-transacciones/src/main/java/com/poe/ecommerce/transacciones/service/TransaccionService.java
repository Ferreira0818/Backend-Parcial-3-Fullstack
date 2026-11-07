package com.poe.ecommerce.transacciones.service;

import com.poe.ecommerce.transacciones.dto.CheckoutRequestDTO;
import com.poe.ecommerce.transacciones.dto.CrearCarritoRequestDTO;
import com.poe.ecommerce.transacciones.dto.ItemCarritoRequestDTO;
import com.poe.ecommerce.transacciones.dto.TransaccionResponseDTO;
import com.poe.ecommerce.transacciones.model.Transaccion;

import java.util.List;

public interface TransaccionService {
    Transaccion obtenerOCrearCarrito(CrearCarritoRequestDTO request);
    Transaccion agregarOActualizarItem(Integer idTransaccion, ItemCarritoRequestDTO itemRequest);
    Transaccion eliminarItem(Integer idTransaccion, Integer productoId);
    Transaccion checkout(Integer idTransaccion, CheckoutRequestDTO checkoutRequest);
    Transaccion cancelarTransaccion(Integer idTransaccion);
    Transaccion obtenerPorId(Integer id);
    TransaccionResponseDTO obtenerDtoPorId(Integer id);
    List<Transaccion> listarTodas(Integer idUsuario, String estado);
    TransaccionResponseDTO mapearAResponseDTO(Transaccion transaccion);
}
