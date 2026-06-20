package com.tup.programacion3.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
