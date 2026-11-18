package com.poe.ecommerce.transacciones.client;

import com.poe.ecommerce.transacciones.dto.UsuarioExternoDTO;
import com.poe.ecommerce.transacciones.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://servicio-usuarios").build();
    }

    public UsuarioExternoDTO obtenerUsuarioPorId(Integer idUsuario) {
        log.info("Llamando a servicio-usuarios para validar IdUsuario: {}", idUsuario);
        return webClient.get()
                .uri("/api/v1/usuarios/{id}", idUsuario)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResourceNotFoundException("El usuario con ID " + idUsuario + " no existe en el sistema"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Error en comunicación con servicio-usuarios"))
                )
                .bodyToMono(UsuarioExternoDTO.class)
                .block();
    }
}
