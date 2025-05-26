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
        // Crear las categorías usando el patrón builder
        List<Categoria> categorias = Arrays.asList(
                Categoria.builder().id(1).denominacion("Alimentos Perros Cachorros").build(),
                Categoria.builder().id(2).denominacion("Alimentos Perros Adultos").build(),
                Categoria.builder().id(3).denominacion("Alimentos Gatos Cachorros").build(),
                Categoria.builder().id(4).denominacion("Alimentos Gatos Adultos").build(),
                Categoria.builder().id(5).denominacion("Alimentos Aves").build(),
                Categoria.builder().id(6).denominacion("Alimentos Peces").build(),
                Categoria.builder().id(7).denominacion("Accesorios generales").build(),
                Categoria.builder().id(8).denominacion("Ropa").build()
        );

        // Guardar las categorías en la base de datos
        categoriaRepository.saveAll(categorias);
    }
}
