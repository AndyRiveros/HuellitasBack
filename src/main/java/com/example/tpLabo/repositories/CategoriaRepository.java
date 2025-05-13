// En un nuevo archivo CategoriaRepository.java

package com.example.tpLabo.repositories;

import com.example.tpLabo.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}