package com.poe.ecommerce.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poe.ecommerce.productos.controller.v1.ProductoController;
import com.poe.ecommerce.productos.dto.DescontarStockDTO;
import com.poe.ecommerce.productos.dto.ProductoRequestDTO;
import com.poe.ecommerce.productos.dto.ProductoResponseDTO;
import com.poe.ecommerce.productos.model.Categoria;
import com.poe.ecommerce.productos.model.Producto;
import com.poe.ecommerce.productos.service.ProductoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("GET /api/v1/productos/{id} debe retornar 200 y el detalle del producto")
    void obtenerPorIdRetornaProducto() throws Exception {
        int id = 1;
        String nombre = "Divine Orb";
        double precio = 1.85;

        ProductoResponseDTO responseDTO = ProductoResponseDTO.builder()
                .idProducto(id)
                .nombreProducto(nombre)
                .precio(precio)
                .stock(500)
                .categoriaId(1)
                .nombreCategoria("Currency")
                .build();

        when(productoService.obtenerDtoPorId(id)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/productos/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(id))
                .andExpect(jsonPath("$.nombreProducto").value(nombre))
                .andExpect(jsonPath("$.precio").value(precio));
    }

    @Test
    @DisplayName("PATCH /api/v1/productos/{id}/descontar-stock debe retornar 200 y el producto actualizado")
    void descontarStockRetorna200() throws Exception {
        int id = 1;
        int cantidadADescontar = 5;

        DescontarStockDTO request = DescontarStockDTO.builder()
                .cantidad(cantidadADescontar)
                .build();

        Categoria cat = Categoria.builder().idCategoria(1).nombreCategoria("Currency").build();
        Producto productoActualizado = Producto.builder()
                .idProducto(id)
                .nombreProducto("Divine Orb")
                .precio(1.85)
                .stock(495)
                .categoria(cat)
                .build();

        ProductoResponseDTO responseDTO = ProductoResponseDTO.builder()
                .idProducto(id)
                .nombreProducto("Divine Orb")
                .precio(1.85)
                .stock(495)
                .categoriaId(1)
                .nombreCategoria("Currency")
                .build();

        when(productoService.descontarStock(eq(id), eq(cantidadADescontar))).thenReturn(productoActualizado);
        when(productoService.mapearAResponseDTO(productoActualizado)).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/productos/{id}/descontar-stock", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(id))
                .andExpect(jsonPath("$.stock").value(495));
    }
}
