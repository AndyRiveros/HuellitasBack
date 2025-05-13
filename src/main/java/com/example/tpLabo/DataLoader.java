package com.example.tpLabo;

import com.example.tpLabo.entities.Categoria;
import com.example.tpLabo.repositories.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public DataLoader(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear las categorías usando el patrón builder
        List<Categoria> categorias = Arrays.asList(
                Categoria.builder().id(1).denominacion("Instrumentos de cuerda").build(),
                Categoria.builder().id(2).denominacion("Percusión").build(),
                Categoria.builder().id(3).denominacion("Instrumentos de viento").build(),
                Categoria.builder().id(4).denominacion("Instrumentos de teclado").build(),
                Categoria.builder().id(5).denominacion("Instrumentos electrónicos").build()
        );

        // Guardar las categorías en la base de datos
        categoriaRepository.saveAll(categorias);
    }
}
