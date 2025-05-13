package com.example.tpLabo.repositories;

import com.example.tpLabo.entities.Instrumento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento, Integer> {
    List<Instrumento> findAll();
    @EntityGraph(attributePaths = "categoria") // Cargar la categoría con el Instrumento
    List<Instrumento> findByCategoriaId(int idCategoria);

}