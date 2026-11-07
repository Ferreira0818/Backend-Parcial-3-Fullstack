package com.poe.ecommerce.transacciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poe.ecommerce.transacciones.controller.v1.TransaccionController;
import com.poe.ecommerce.transacciones.dto.CrearCarritoRequestDTO;
import com.poe.ecommerce.transacciones.dto.TransaccionResponseDTO;
import com.poe.ecommerce.transacciones.model.Estado;
import com.poe.ecommerce.transacciones.model.Transaccion;
import com.poe.ecommerce.transacciones.service.TransaccionService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransaccionController.class)
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransaccionService transaccionService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("POST /api/v1/transacciones/carrito debe retornar 201 Created y el carrito")
    void crearCarritoRetorna201() throws Exception {
        int idUsuario = 1;
        CrearCarritoRequestDTO request = CrearCarritoRequestDTO.builder()
                .idUsuario(idUsuario)
                .build();

        Estado estado = Estado.builder().idEstado(1).nombreEstado("EN_CARRITO").build();
        Transaccion transaccion = Transaccion.builder()
                .idTransaccion(1)
                .idUsuario(idUsuario)
                .estado(estado)
                .total(0.0)
                .fechaTransaccion(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        TransaccionResponseDTO responseDTO = TransaccionResponseDTO.builder()
                .idTransaccion(1)
                .idUsuario(idUsuario)
                .estado("EN_CARRITO")
                .total(0.0)
                .fechaTransaccion(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        when(transaccionService.obtenerOCrearCarrito(any(CrearCarritoRequestDTO.class))).thenReturn(transaccion);
        when(transaccionService.mapearAResponseDTO(transaccion)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/transacciones/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTransaccion").value(1))
                .andExpect(jsonPath("$.estado").value("EN_CARRITO"))
                .andExpect(jsonPath("$.idUsuario").value(idUsuario));
    }

    @Test
    @DisplayName("GET /api/v1/transacciones/{id} debe retornar 200 y el detalle de la transacción")
    void obtenerPorIdRetornaTransaccion() throws Exception {
        int id = 5;
        TransaccionResponseDTO responseDTO = TransaccionResponseDTO.builder()
                .idTransaccion(id)
                .idUsuario(1)
                .estado("PAGADO")
                .medioDePago("PAYPAL")
                .total(150.0)
                .fechaTransaccion(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        when(transaccionService.obtenerDtoPorId(id)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/transacciones/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransaccion").value(id))
                .andExpect(jsonPath("$.estado").value("PAGADO"))
                .andExpect(jsonPath("$.medioDePago").value("PAYPAL"))
                .andExpect(jsonPath("$.total").value(150.0));
    }
}
