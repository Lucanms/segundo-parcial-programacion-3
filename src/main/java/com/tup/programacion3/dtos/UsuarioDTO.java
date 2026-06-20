package com.tup.programacion3.dtos;

public record UsuarioDTO(
        Long id,
        String nombre,
        String apellido,
        String mail,
        String celular
) {
}
