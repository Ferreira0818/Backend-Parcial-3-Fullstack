package com.poe.ecommerce.productos.service;

import com.poe.ecommerce.productos.dto.ProductoRequestDTO;
import com.poe.ecommerce.productos.exception.BadRequestException;
import com.poe.ecommerce.productos.exception.ResourceNotFoundException;
import com.poe.ecommerce.productos.exception.StockInsuficienteException;
import com.poe.ecommerce.productos.model.Categoria;
import com.poe.ecommerce.productos.model.Producto;
import com.poe.ecommerce.productos.repository.CategoriaRepository;
import com.poe.ecommerce.productos.repository.ProductoRepository;
import com.poe.ecommerce.productos.service.impl.ProductoServiceImpl;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Faker faker;
    private Categoria categoriaCurrency;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        categoriaCurrency = Categoria.builder()
                .idCategoria(1)
                .nombreCategoria("Currency")
                .build();
    }

    @Test
    @DisplayName("Debe crear un producto correctamente con datos generados por Datafaker")
    void crearProductoExitoso() {
        String fakeNombre = "Orb of " + faker.color().name() + " " + faker.number().digits(3);
        double fakePrecio = faker.number().randomDouble(2, 1, 100);
        int fakeStock = faker.number().numberBetween(10, 500);

        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombreProducto(fakeNombre)
                .precio(fakePrecio)
                .stock(fakeStock)
                .categoriaId(1)
                .imagen("http://example.com/item.png")
                .alt("PoE currency")
                .descripcion(faker.lorem().sentence())
                .build();

        Producto productoEsperado = Producto.builder()
                .idProducto(50)
                .nombreProducto(fakeNombre)
                .precio(fakePrecio)
                .stock(fakeStock)
                .categoria(categoriaCurrency)
                .build();

        when(productoRepository.existsByNombreProducto(fakeNombre)).thenReturn(false);
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaCurrency));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEsperado);

        Producto resultado = productoService.crear(request);

        assertNotNull(resultado);
        assertEquals(50, resultado.getIdProducto());
        assertEquals(fakeNombre, resultado.getNombreProducto());
        assertEquals(fakePrecio, resultado.getPrecio());
        assertEquals(fakeStock, resultado.getStock());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe descontar stock exitosamente cuando hay existencias suficientes")
    void descontarStockExitoso() {
        int idProducto = 1;
        int stockInicial = 100;
        int cantidadADescontar = 15;

        Producto producto = Producto.builder()
                .idProducto(idProducto)
                .nombreProducto("Divine Orb")
                .stock(stockInicial)
                .precio(1.85)
                .categoria(categoriaCurrency)
                .build();

        when(productoRepository.findById(idProducto)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.descontarStock(idProducto, cantidadADescontar);

        assertEquals(85, resultado.getStock());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    @DisplayName("Debe lanzar StockInsuficienteException cuando la cantidad supera el stock disponible")
    void descontarStockInsuficienteLanzaError() {
        int idProducto = 1;
        int stockInicial = 5;
        int cantidadADescontar = 10;

        Producto producto = Producto.builder()
                .idProducto(idProducto)
                .nombreProducto("Mirror of Kalandra")
                .stock(stockInicial)
                .precio(280.0)
                .categoria(categoriaCurrency)
                .build();

        when(productoRepository.findById(idProducto)).thenReturn(Optional.of(producto));

        assertThrows(StockInsuficienteException.class, () ->
                productoService.descontarStock(idProducto, cantidadADescontar)
        );
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException al intentar crear un producto con nombre repetido")
    void crearProductoDuplicadoLanzaError() {
        String nombreExistente = "Chaos Orb";
        ProductoRequestDTO request = ProductoRequestDTO.builder()
                .nombreProducto(nombreExistente)
                .precio(0.05)
                .stock(100)
                .categoriaId(1)
                .build();

        when(productoRepository.existsByNombreProducto(nombreExistente)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> productoService.crear(request));
        verify(productoRepository, never()).save(any());
    }
}
