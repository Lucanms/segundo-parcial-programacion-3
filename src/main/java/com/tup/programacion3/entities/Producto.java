package com.tup.programacion3.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
public class Producto extends Base {

    private String nombre;

    private Double precio;

    private String descripcion;

    private int stock;

    private String imagen;

    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}