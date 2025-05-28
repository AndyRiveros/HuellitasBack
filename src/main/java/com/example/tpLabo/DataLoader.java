package com.example.tpLabo;

import com.example.tpLabo.entities.Categoria;
import com.example.tpLabo.repositories.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
//
@Component
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public DataLoader(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Categoria> categorias = Arrays.asList(
                Categoria.builder().id(1).denominacion("Alimentos").build(),
                Categoria.builder().id(2).denominacion("Accesorios").build(),
                Categoria.builder().id(3).denominacion("Salud").build(),
                Categoria.builder().id(4).denominacion("Estética e Higiene").build(),
                Categoria.builder().id(5).denominacion("Snacks").build(),
                Categoria.builder().id(6).denominacion("Ofertas").build()
        );
        categoriaRepository.saveAll(categorias);
    }
}
