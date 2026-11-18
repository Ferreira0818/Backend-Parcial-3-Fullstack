package com.poe.ecommerce.transacciones.client;

import com.poe.ecommerce.transacciones.dto.DescontarStockDTO;
import com.poe.ecommerce.transacciones.dto.ProductoExternoDTO;
import com.poe.ecommerce.transacciones.exception.BadRequestException;
import com.poe.ecommerce.transacciones.exception.ResourceNotFoundException;
import com.poe.ecommerce.transacciones.exception.TransaccionInvalidaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://servicio-productos").build();
    }

    public ProductoExternoDTO obtenerProductoPorId(Integer idProducto) {
        log.info("Llamando a servicio-productos para consultar ProductoId: {}", idProducto);
        return webClient.get()
                .uri("/api/v1/productos/{id}", idProducto)
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), clientResponse ->
                        Mono.error(new ResourceNotFoundException("El producto con ID " + idProducto + " no existe en el catálogo"))
                )
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new BadRequestException("Error al consultar el producto con ID " + idProducto))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Error en comunicación con servicio-productos"))
                )
                .bodyToMono(ProductoExternoDTO.class)
                .block();
    }

    public ProductoExternoDTO descontarStock(Integer idProducto, int cantidad) {
        log.info("Llamando a servicio-productos para descontar {} unidades de ProductoId: {}", cantidad, idProducto);
        DescontarStockDTO request = DescontarStockDTO.builder().cantidad(cantidad).build();

        return webClient.patch()
                .uri("/api/v1/productos/{id}/descontar-stock", idProducto)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.CONFLICT), clientResponse ->
                        Mono.error(new TransaccionInvalidaException("Stock insuficiente para el producto ID " + idProducto))
                )
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), clientResponse ->
                        Mono.error(new ResourceNotFoundException("El producto con ID " + idProducto + " no existe"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Error en comunicación con servicio-productos al descontar stock"))
                )
                .bodyToMono(ProductoExternoDTO.class)
                .block();
    }
}
