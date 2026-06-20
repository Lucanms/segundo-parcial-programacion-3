package com.tup.programacion3.entities;

import com.tup.programacion3.enums.Rol;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
public class Usuario extends Base {

    private String nombre;

    private String apellido;

    @Column(unique = true)
    private String mail;

    private String celular;

    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    @Builder.Default
    private Set<Pedido> pedidos = new HashSet<>();

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }
}