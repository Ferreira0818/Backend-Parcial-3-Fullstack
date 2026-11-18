package com.poe.ecommerce.transacciones.dto;

import lombok.*;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "transacciones", itemRelation = "transaccion")
public class TransaccionResponseDTO {
    private Integer idTransaccion;
    private Integer idUsuario;
    private UsuarioExternoDTO usuario;
    private Double total;
    private String estado;
    private LocalDateTime fechaTransaccion;
    private String medioDePago;
    private List<ItemTransaccionResponseDTO> items;
}
