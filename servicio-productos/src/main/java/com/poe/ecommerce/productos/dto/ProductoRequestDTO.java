package com.poe.ecommerce.productos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 120, message = "El nombre no debe superar los 120 caracteres")
    private String nombreProducto;

    @Size(max = 255, message = "La URL de la imagen no debe superar los 255 caracteres")
    private String imagen;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private Double precio;

    @Size(max = 150, message = "El texto alternativo no debe superar los 150 caracteres")
    private String alt;

    @Size(max = 500, message = "La descripción no debe superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El ID de categoría es obligatorio")
    private Integer categoriaId;
}
