package com.tup.programacion3.entities;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Categoria extends Base {

    private String nombre;
    private String descripcion;
}
