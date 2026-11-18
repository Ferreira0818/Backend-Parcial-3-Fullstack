package com.poe.ecommerce.productos.service.impl;

import com.poe.ecommerce.productos.dto.ProductoRequestDTO;
import com.poe.ecommerce.productos.dto.ProductoResponseDTO;
import com.poe.ecommerce.productos.exception.BadRequestException;
import com.poe.ecommerce.productos.exception.ResourceNotFoundException;
import com.poe.ecommerce.productos.exception.StockInsuficienteException;
import com.poe.ecommerce.productos.model.Categoria;
import com.poe.ecommerce.productos.model.Producto;
import com.poe.ecommerce.productos.repository.CategoriaRepository;
import com.poe.ecommerce.productos.repository.ProductoRepository;
import com.poe.ecommerce.productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerDtoPorId(Integer id) {
        return mapearAResponseDTO(obtenerPorId(id));
    }

    @Override
    @Transactional
    public Producto crear(ProductoRequestDTO request) {
        if (productoRepository.existsByNombreProducto(request.getNombreProducto())) {
            throw new BadRequestException("Ya existe un producto con el nombre: " + request.getNombreProducto());
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + request.getCategoriaId() + " no encontrada"));

        Producto producto = Producto.builder()
                .nombreProducto(request.getNombreProducto().trim())
                .imagen(request.getImagen())
                .precio(request.getPrecio())
                .alt(request.getAlt())
                .descripcion(request.getDescripcion())
                .stock(request.getStock())
                .categoria(categoria)
                .build();

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizar(Integer id, ProductoRequestDTO request) {
        Producto producto = obtenerPorId(id);

        String nuevoNombre = request.getNombreProducto().trim();
        if (!producto.getNombreProducto().equalsIgnoreCase(nuevoNombre) &&
                productoRepository.existsByNombreProducto(nuevoNombre)) {
            throw new BadRequestException("Ya existe otro producto con el nombre: " + nuevoNombre);
        }

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + request.getCategoriaId() + " no encontrada"));

        producto.setNombreProducto(nuevoNombre);
        producto.setImagen(request.getImagen());
        producto.setPrecio(request.getPrecio());
        producto.setAlt(request.getAlt());
        producto.setDescripcion(request.getDescripcion());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto descontarStock(Integer id, int cantidad) {
        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad a descontar debe ser mayor a 0");
        }

        Producto producto = obtenerPorId(id);

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente para el producto '" +
                    producto.getNombreProducto() + "'. Stock disponible: " + producto.getStock() +
                    ", cantidad solicitada: " + cantidad);
        }

        producto.setStock(producto.getStock() - cantidad);
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }

    @Override
    public ProductoResponseDTO mapearAResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .idProducto(producto.getIdProducto())
                .nombreProducto(producto.getNombreProducto())
                .imagen(producto.getImagen())
                .precio(producto.getPrecio())
                .alt(producto.getAlt())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getIdCategoria() : null)
                .nombreCategoria(producto.getCategoria() != null ? producto.getCategoria().getNombreCategoria() : null)
                .build();
    }
}
