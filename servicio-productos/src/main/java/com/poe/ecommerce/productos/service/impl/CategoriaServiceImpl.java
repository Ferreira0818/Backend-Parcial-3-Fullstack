package com.poe.ecommerce.productos.service.impl;

import com.poe.ecommerce.productos.dto.CategoriaDTO;
import com.poe.ecommerce.productos.dto.CategoriaRequestDTO;
import com.poe.ecommerce.productos.exception.BadRequestException;
import com.poe.ecommerce.productos.exception.ResourceNotFoundException;
import com.poe.ecommerce.productos.model.Categoria;
import com.poe.ecommerce.productos.repository.CategoriaRepository;
import com.poe.ecommerce.productos.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria obtenerPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + id + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerDtoPorId(Integer id) {
        Categoria categoria = obtenerPorId(id);
        return CategoriaDTO.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombreCategoria(categoria.getNombreCategoria())
                .build();
    }

    @Override
    @Transactional
    public Categoria crear(CategoriaRequestDTO request) {
        if (categoriaRepository.existsByNombreCategoria(request.getNombreCategoria())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + request.getNombreCategoria());
        }
        Categoria categoria = Categoria.builder()
                .nombreCategoria(request.getNombreCategoria().trim())
                .build();
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public Categoria actualizar(Integer id, CategoriaRequestDTO request) {
        Categoria categoria = obtenerPorId(id);
        String nuevoNombre = request.getNombreCategoria().trim();
        if (!categoria.getNombreCategoria().equalsIgnoreCase(nuevoNombre) &&
                categoriaRepository.existsByNombreCategoria(nuevoNombre)) {
            throw new BadRequestException("Ya existe otra categoría con el nombre: " + nuevoNombre);
        }
        categoria.setNombreCategoria(nuevoNombre);
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Categoria categoria = obtenerPorId(id);
        categoriaRepository.delete(categoria);
    }
}
