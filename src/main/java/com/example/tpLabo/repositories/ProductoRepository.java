package com.example.tpLabo.repositories;

import com.example.tpLabo.entities.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findAll();
    @EntityGraph(attributePaths = "categoria") // Cargar la categoría con el producto
    List<Producto> findByCategoriaId(int idCategoria);

}