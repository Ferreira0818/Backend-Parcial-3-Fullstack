package com.poe.ecommerce.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poe.ecommerce.usuarios.controller.v1.UsuarioController;
import com.poe.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.poe.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.poe.ecommerce.usuarios.model.Rol;
import com.poe.ecommerce.usuarios.model.Usuario;
import com.poe.ecommerce.usuarios.service.UsuarioService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/{id} debe retornar 200 y el usuario")
    void obtenerPorIdRetornaUsuario() throws Exception {
        int id = 1;
        String username = faker.name().username();
        String email = faker.internet().emailAddress();

        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .idUsuario(id)
                .nombreUsuario(username)
                .email(email)
                .fechaRegistro(LocalDateTime.now())
                .idRol(1)
                .nombreRol("CLIENTE")
                .build();

        when(usuarioService.obtenerDtoPorId(id)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/usuarios/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(id))
                .andExpect(jsonPath("$.nombreUsuario").value(username))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @DisplayName("POST /api/v1/usuarios con datos válidos retorna 201 Created")
    void crearUsuarioRetorna201() throws Exception {
        String username = faker.name().username();
        String email = faker.internet().emailAddress();
        String password = "password123";

        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .nombreUsuario(username)
                .email(email)
                .contrasena(password)
                .idRol(1)
                .build();

        Rol rol = Rol.builder().idRol(1).nombreRol("CLIENTE").build();
        Usuario creado = Usuario.builder()
                .idUsuario(1)
                .nombreUsuario(username)
                .email(email)
                .contrasena(password)
                .rol(rol)
                .fechaRegistro(LocalDateTime.now())
                .build();

        UsuarioResponseDTO responseDTO = UsuarioResponseDTO.builder()
                .idUsuario(1)
                .nombreUsuario(username)
                .email(email)
                .idRol(1)
                .nombreRol("CLIENTE")
                .build();

        when(usuarioService.crear(any(UsuarioRequestDTO.class))).thenReturn(creado);
        when(usuarioService.mapearAResponseDTO(creado)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.nombreUsuario").value(username));
    }
}
