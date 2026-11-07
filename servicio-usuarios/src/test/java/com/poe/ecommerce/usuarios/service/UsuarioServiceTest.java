package com.poe.ecommerce.usuarios.service;

import com.poe.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.poe.ecommerce.usuarios.exception.BadRequestException;
import com.poe.ecommerce.usuarios.exception.ResourceNotFoundException;
import com.poe.ecommerce.usuarios.model.Rol;
import com.poe.ecommerce.usuarios.model.Usuario;
import com.poe.ecommerce.usuarios.repository.RolRepository;
import com.poe.ecommerce.usuarios.repository.UsuarioRepository;
import com.poe.ecommerce.usuarios.service.impl.UsuarioServiceImpl;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Faker faker;
    private Rol rolCliente;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        rolCliente = Rol.builder()
                .idRol(1)
                .nombreRol("CLIENTE")
                .build();
    }

    @Test
    @DisplayName("Debe crear un usuario exitosamente con datos generados por Datafaker")
    void crearUsuarioExitoso() {
        // Datos generados con Datafaker
        String fakeUsername = faker.name().username();
        String fakeEmail = faker.internet().emailAddress();
        String fakePassword = faker.internet().password(8, 16);

        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .nombreUsuario(fakeUsername)
                .email(fakeEmail)
                .contrasena(fakePassword)
                .idRol(1)
                .build();

        Usuario usuarioEsperado = Usuario.builder()
                .idUsuario(10)
                .nombreUsuario(fakeUsername)
                .email(fakeEmail)
                .contrasena(fakePassword)
                .fechaRegistro(LocalDateTime.now())
                .rol(rolCliente)
                .build();

        when(usuarioRepository.existsByEmail(fakeEmail)).thenReturn(false);
        when(rolRepository.findById(1)).thenReturn(Optional.of(rolCliente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEsperado);

        Usuario resultado = usuarioService.crear(request);

        assertNotNull(resultado);
        assertEquals(10, resultado.getIdUsuario());
        assertEquals(fakeUsername, resultado.getNombreUsuario());
        assertEquals(fakeEmail, resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el email ya existe")
    void crearUsuarioConEmailDuplicadoLanzaError() {
        String fakeEmail = faker.internet().emailAddress();

        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .nombreUsuario(faker.name().username())
                .email(fakeEmail)
                .contrasena(faker.internet().password())
                .idRol(1)
                .build();

        when(usuarioRepository.existsByEmail(fakeEmail)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> usuarioService.crear(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el usuario no existe")
    void obtenerUsuarioNoExistenteLanzaError() {
        int fakeId = faker.number().positive();
        when(usuarioRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(fakeId));
    }
}
