package com.example.tpLabo.controllers;

import com.example.tpLabo.entities.Categoria;
import com.example.tpLabo.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // reemplaza con la URL de tu aplicación React
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/api/categorias")
    public List<Categoria> getCategorias() {
        return categoriaService.findAll();
    }
}