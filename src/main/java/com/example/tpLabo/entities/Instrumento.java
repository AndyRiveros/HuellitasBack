package com.example.tpLabo.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String instrumento;
    private String marca;
    private String modelo;
    private String imagen;
    private String precio;
    private String costoEnvio;
    private Integer cantidadVendida;
    private String descripcion;

    public Instrumento(String instrumento, String marca, String modelo, String precio) {
        this.instrumento = instrumento;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
    }

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public boolean getIsDeleted() {
    return this.isDeleted;
}

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    @ManyToOne
    @JoinColumn(name = "idCategoria", referencedColumnName = "id")
    @JsonBackReference
    private Categoria categoria;

//    @OneToMany(mappedBy = "instrumento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference(value = "instrumento-detalles")
//    private List<PedidoDetalle> detalles;

    @Transient
    public Integer getIdCategoria() {
        return categoria != null ? categoria.getId() : null;
    }
}