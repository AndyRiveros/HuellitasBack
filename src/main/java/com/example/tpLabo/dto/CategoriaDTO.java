package com.example.tpLabo.dto;

import lombok.Data;

@Data
public class CategoriaDTO {
    private int id;
    private String denominacion;
    private Integer id_padre; // Puede ser null
}