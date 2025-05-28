package com.example.tpLabo.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String producto;
    private String marca;
    private String modelo;
    private String imagen;
    private String precio;
    private String costoEnvio;
    private Integer cantidadVendida;
    private String descripcion;
    private String especie;
    private String tipo;
    private String etapa;


    public Producto(String producto, String marca, String modelo, String precio) {
        this.producto = producto;
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

//    @OneToMany(mappedBy = "Producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference(value = "Producto-detalles")
//    private List<PedidoDetalle> detalles;

    @Transient
    public Integer getIdCategoria() {
        return categoria != null ? categoria.getId() : null;
    }
}