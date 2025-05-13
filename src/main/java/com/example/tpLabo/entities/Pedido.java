package com.example.tpLabo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date fechaPedido;
    private Double totalPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "pedido-detalles")
    private List<PedidoDetalle> pedidoDetalles;

    public void addPedidoDetalle(PedidoDetalle pedidoDetalle) {
        this.pedidoDetalles.add(pedidoDetalle);
        pedidoDetalle.setPedido(this); // Asigna el pedido al detalle
    }
}
