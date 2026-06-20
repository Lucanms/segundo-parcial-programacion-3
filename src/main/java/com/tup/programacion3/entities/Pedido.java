package com.tup.programacion3.entities;

import com.tup.programacion3.enums.Estado;
import com.tup.programacion3.enums.FormaPago;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
public class Pedido extends Base implements Calculable {

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Double total;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private Set<DetallePedido> detalles = new HashSet<>();

    public void addDetallePedido(DetallePedido detalle) {
        detalles.add(detalle);
    }

    @Override
    public void calcularTotal() {
        total = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }
}