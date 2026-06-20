package com.tup.programacion3;
import com.tup.programacion3.dtos.UsuarioDTO;
import com.tup.programacion3.entities.*;
import com.tup.programacion3.enums.*;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        // Categorías
        Categoria tecnologia = Categoria.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Tecnología")
                .descripcion("Productos tecnológicos")
                .build();

        Categoria ropa = Categoria.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Ropa")
                .descripcion("Indumentaria")
                .build();

        Categoria hogar = Categoria.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Hogar")
                .descripcion("Artículos para el hogar")
                .build();


        // Productos
        Set<Producto> productos = new HashSet<>();
        for (long i = 1; i <= 10; i++) {

            Categoria categoria;

            if (i <= 3) {
                categoria = tecnologia;
            } else if (i <= 6) {
                categoria = ropa;
            } else {
                categoria = hogar;
            }

            productos.add(
                    Producto.builder()
                            .eliminado(false)
                            .createdAt(LocalDateTime.now())
                            .nombre("Producto " + i)
                            .precio(i * 100.0)
                            .descripcion("Descripción producto " + i)
                            .stock(10)
                            .imagen("img" + i + ".jpg")
                            .disponible(true)
                            .categoria(categoria)
                            .build()
            );
        }

        Producto productoReferencia = productos.iterator().next();

        // Usuarios

        Usuario usuario1 = Usuario.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Juan")
                .apellido("Perez")
                .mail("juan@mail.com")
                .celular("111111111")
                .contrasenia("1234")
                .rol(Rol.USUARIO)
                .build();

        Usuario usuario2 = Usuario.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Ana")
                .apellido("Ramirez")
                .mail("ana@mail.com")
                .celular("222222222")
                .contrasenia("4321")
                .rol(Rol.ADMIN)
                .build();

        // Pedidos

        Pedido pedido1 = Pedido.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .fecha(LocalDate.now())
                .estado(Estado.PENDIENTE)
                .formaPago(FormaPago.TARJETA)
                .build();

        Pedido pedido2 = Pedido.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .fecha(LocalDate.now())
                .estado(Estado.CONFIRMADO)
                .formaPago(FormaPago.TRANSFERENCIA)
                .build();

        Pedido pedido3 = Pedido.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .fecha(LocalDate.now())
                .estado(Estado.TERMINADO)
                .formaPago(FormaPago.EFECTIVO)
                .build();



        // Detalles

        pedido1.addDetallePedido(
                DetallePedido.builder()
                        .producto(productoReferencia)
                        .cantidad(2)
                        .subtotal(productoReferencia.getPrecio() * 2)
                        .build()
        );

        pedido1.addDetallePedido(
                DetallePedido.builder()
                        .producto(
                                productos.stream().skip(1).findFirst().orElse(null)
                        )
                        .cantidad(1)
                        .subtotal(500.0)
                        .build()
        );

        pedido2.addDetallePedido(
                DetallePedido.builder()
                        .producto(
                                productos.stream().skip(2).findFirst().orElse(null)
                        )
                        .cantidad(3)
                        .subtotal(900.0)
                        .build()
        );

        pedido2.addDetallePedido(
                DetallePedido.builder()
                        .producto(
                                productos.stream().skip(3).findFirst().orElse(null)
                        )
                        .cantidad(2)
                        .subtotal(700.0)
                        .build()
        );

        pedido3.addDetallePedido(
                DetallePedido.builder()
                        .producto(
                                productos.stream().skip(4).findFirst().orElse(null)
                        )
                        .cantidad(1)
                        .subtotal(300.0)
                        .build()
        );

        pedido3.addDetallePedido(
                DetallePedido.builder()
                        .producto(
                                productos.stream().skip(5).findFirst().orElse(null)
                        )
                        .cantidad(4)
                        .subtotal(1600.0)
                        .build()
        );

        pedido1.calcularTotal();
        pedido2.calcularTotal();
        pedido3.calcularTotal();

        // Asignar pedidos
        usuario1.agregarPedido(pedido1);
        usuario1.agregarPedido(pedido2);

        usuario2.agregarPedido(pedido3);


        // Mostrar un producto
        System.out.println("PRODUCTO:");
        System.out.println(productoReferencia);

        // Mostrar todos los productos

        System.out.println("\nLISTA PRODUCTOS:");
        System.out.println(productos);

        // Usuario con más pedidos
        Usuario mayor = usuario1.getPedidos().size() > usuario2.getPedidos().size() ? usuario1 : usuario2;

        System.out.println("\nUSUARIO CON MÁS PEDIDOS:");
        System.out.println(mayor.getPedidos());

        // Comparación equals
        Producto nuevoProducto = Producto.builder()
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .nombre("Producto Copia")
                .precio(9999.0)
                .descripcion("Producto duplicado")
                .stock(1)
                .imagen("copia.jpg")
                .disponible(true)
                .categoria(tecnologia)
                .build();


        System.out.println("\nCOMPARACIONES EQUALS:");

        for (Producto producto : productos) {

            System.out.println(
                    producto.getNombre()
                            + " -> "
                            + producto.equals(nuevoProducto)
            );
        }

        // Dto

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario1.getId(),
                usuario1.getNombre(),
                usuario1.getApellido(),
                usuario1.getMail(),
                usuario1.getCelular()
        );

        System.out.println("\nDTO:");
        System.out.println(usuarioDTO);



        // Hay que borrar el archivo de data antes de ejecutar
        // Creacion del EntityManager

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("miUnidad");

        EntityManager em = emf.createEntityManager();


        // Persistir categorías

        em.getTransaction().begin();

        em.persist(tecnologia);
        em.persist(ropa);
        em.persist(hogar);

        em.getTransaction().commit();



        // Persistir productos

        em.getTransaction().begin();

        for (Producto p : productos) {
            em.persist(p);
        }

        em.getTransaction().commit();



        // Persistir usuarios

        em.getTransaction().begin();

        em.persist(usuario1);
        em.persist(usuario2);

        em.getTransaction().commit();


        // Actualizar 2 productos

        em.getTransaction().begin();

        Producto p1 = em.find(Producto.class, 1L);
        p1.setPrecio(9999.0);

        Producto p2 = em.find(Producto.class, 2L);
        p2.setStock(50);

        em.getTransaction().commit();



        // Buscar usuario por id

        Usuario usuario = em.find(Usuario.class, 1L);

        System.out.println(usuario);



        // Buscar usuario por mail

        Usuario usuarioMail = em.createQuery(
                        "SELECT u FROM Usuario u WHERE u.mail = :mail",
                        Usuario.class
                )
                .setParameter("mail", "juan@mail.com")
                .getSingleResult();

        System.out.println(usuarioMail);


        // Borrar un producto

        em.getTransaction().begin();

        Producto producto = em.find(Producto.class, 10L);

        if(producto != null){
            em.remove(producto);
        }

        em.getTransaction().commit();



    }
}
