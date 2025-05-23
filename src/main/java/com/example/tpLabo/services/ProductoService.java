package com.example.tpLabo.services;

import com.example.tpLabo.entities.Producto;
import com.example.tpLabo.repositories.ProductoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto findById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public List<Producto> findAllDeleted() {
        return productoRepository.findAll().stream()
                .filter(Producto::getIsDeleted)
                .collect(Collectors.toList());
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto update(Integer id, Producto producto) {
        Producto existingProducto = findById(id);
        BeanUtils.copyProperties(producto, existingProducto, "id");
        return productoRepository.save(existingProducto);
    }

    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> findByCategoria(int idCategoria) {
        return productoRepository.findByCategoriaId(idCategoria);
    }

    // Método para buscar productos por nombre o descripción
    public List<Producto> buscarPorNombreODescripcion(String query) {
        return productoRepository.findByProductoContainingIgnoreCaseOrDescripcionContainingIgnoreCase(query, query);
    }
}
