package com.tup.programacion3;

import com.tup.programacion3.entities.Categoria;
import com.tup.programacion3.entities.Producto;
import com.tup.programacion3.repository.CategoriaRepository;
import com.tup.programacion3.repository.ProductoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CategoriaRepository categoriaRepo = new CategoriaRepository();
    private static final ProductoRepository productoRepo = new ProductoRepository();

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n----------------------------------------");
            System.out.println("         MENÚ PRINCIPAL");
            System.out.println("----------------------------------------");
            System.out.println("1. Categorías");
            System.out.println("2. Productos");
            System.out.println("3. Reportes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> menuCategorias();
                case "2" -> menuProductos();
                case "3" -> menuReportes();
                case "0" -> {
                    salir = true;
                    System.out.println("¡Chau!");
                }
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }
        }


    }


    // Submenu de categorias
    private static void menuCategorias() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- CATEGORÍAS ---");
            System.out.println("1. Alta");
            System.out.println("2. Modificación");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> altaCategoria();
                case "2" -> modificarCategoria();
                case "3" -> bajaCategoria();
                case "4" -> listarCategorias();
                case "0" -> volver = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }


    /** Alta de categoria */
    private static void altaCategoria() {
        System.out.println("\n--- ALTA DE CATEGORÍA ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("Error: el nombre no puede estar vacío. Operación cancelada.");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        Categoria nueva = Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .build();

        Categoria guardada = categoriaRepo.guardar(nueva);
        System.out.println("Categoría creada exitosamente. ID generado: " + guardada.getId());
    }


    /** Modificacion de categoria */
    private static void modificarCategoria() {
        System.out.println("\n--- MODIFICACIÓN DE CATEGORÍA ---");

        List<Categoria> activas = categoriaRepo.listarActivos();
        if (activas.isEmpty()) {
            System.out.println("No hay categorías activas.");
            return;
        }

        System.out.println("Categorías activas:");
        for (Categoria c : activas) {
            System.out.println("  [" + c.getId() + "] " + c.getNombre() + " - " + c.getDescripcion());
        }

        System.out.print("Ingrese el ID de la categoría a modificar: ");
        Long id = parsearLong(scanner.nextLine().trim());
        if (id == null) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Categoria> opt = categoriaRepo.buscarPorId(id);
        if (opt.isEmpty() || opt.get().isEliminado()) {
            System.out.println("Error: no se encontró una categoría activa con el ID " + id + ".");
            return;
        }

        Categoria categoria = opt.get();
        System.out.println("Valores actuales → Nombre: " + categoria.getNombre()
                + " | Descripción: " + categoria.getDescripcion());
        System.out.println("(Dejar en blanco para conservar el valor actual)");

        System.out.print("Nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();
        if (!nuevoNombre.isEmpty()) {
            categoria.setNombre(nuevoNombre);
        }

        System.out.print("Nueva descripción: ");
        String nuevaDesc = scanner.nextLine().trim();
        if (!nuevaDesc.isEmpty()) {
            categoria.setDescripcion(nuevaDesc);
        }

        categoriaRepo.guardar(categoria);
        System.out.println("Categoría actualizada correctamente.");
    }


    /** Baja logica de categoria */

    private static void bajaCategoria() {
        System.out.println("\n--- BAJA DE CATEGORÍA ---");

        System.out.print("Ingrese el ID de la categoría a dar de baja: ");
        Long id = parsearLong(scanner.nextLine().trim());
        if (id == null) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Categoria> opt = categoriaRepo.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Error: no existe ninguna categoría con el ID " + id + ".");
            return;
        }

        Categoria categoria = opt.get();
        if (categoria.isEliminado()) {
            System.out.println("Error: la categoría \"" + categoria.getNombre() + "\" ya está dada de baja.");
            return;
        }

        boolean resultado = categoriaRepo.eliminarLogico(id);
        if (resultado) {
            System.out.println("Categoría \"" + categoria.getNombre() + "\" dada de baja exitosamente.");
        } else {
            System.out.println("Error: no se pudo dar de baja la categoría.");
        }
    }


    /** Listado de categorias activas */
    private static void listarCategorias() {
        System.out.println("\n--- LISTADO DE CATEGORÍAS ACTIVAS ---");

        List<Categoria> activas = categoriaRepo.listarActivos();
        if (activas.isEmpty()) {
            System.out.println("No hay categorías activas.");
            return;
        }

        System.out.printf("%-5s %-20s %-40s%n", "ID", "Nombre", "Descripción");
        System.out.println("-".repeat(65));
        for (Categoria c : activas) {
            System.out.printf("%-5d %-20s %-40s%n", c.getId(), c.getNombre(), c.getDescripcion());
        }
    }


    // Submenu de productos
    private static void menuProductos() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- PRODUCTOS ---");
            System.out.println("1. Alta");
            System.out.println("2. Modificación");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> altaProducto();
                case "2" -> modificarProducto();
                case "3" -> bajaProducto();
                case "4" -> listarProductos();
                case "0" -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    /** Alta de producto */
    private static void altaProducto() {
        System.out.println("\n--- ALTA DE PRODUCTO ---");

        List<Categoria> categorias = categoriaRepo.listarActivos();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías activas. Operación cancelada.");
            return;
        }

        System.out.println("Categorías disponibles:");
        for (Categoria c : categorias) {
            System.out.println("  [" + c.getId() + "] " + c.getNombre());
        }

        System.out.print("Seleccione el ID de la categoría: ");
        Long catId = parsearLong(scanner.nextLine().trim());
        if (catId == null) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Categoria> optCat = categoriaRepo.buscarPorId(catId);
        if (optCat.isEmpty() || optCat.get().isEliminado()) {
            System.out.println("Error: categoría no encontrada.");
            return;
        }
        Categoria categoria = optCat.get();

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("Error: el nombre no puede estar vacío.");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        System.out.print("Precio (mayor a 0): ");
        Double precio = parsearDouble(scanner.nextLine().trim());
        if (precio == null || precio <= 0) {
            System.out.println("Error: el precio debe ser mayor a 0. Operación cancelada.");
            return;
        }

        System.out.print("Stock (mayor o igual a 0): ");
        Integer stock = parsearInt(scanner.nextLine().trim());
        if (stock == null || stock < 0) {
            System.out.println("Error: el stock no puede ser negativo. Operación cancelada.");
            return;
        }

        Producto nuevo = Producto.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .precio(precio)
                .stock(stock)
                .imagen("")
                .disponible(true)
                .categoria(categoria)
                .eliminado(false)
                .createdAt(LocalDateTime.now())
                .build();

        Producto guardado = productoRepo.guardar(nuevo);
        System.out.println("Producto creado exitosamente. ID generado: " + guardado.getId()
                + " | Categoría: " + categoria.getNombre());
    }

    /** Modificacion de producto */
    private static void modificarProducto() {
        System.out.println("\n--- MODIFICACIÓN DE PRODUCTO ---");

        List<Producto> activos = productoRepo.listarActivos();
        if (activos.isEmpty()) {
            System.out.println("No hay productos activos.");
            return;
        }

        System.out.println("Productos activos:");
        for (Producto p : activos) {
            System.out.printf("  [%d] %s | Precio: $%.2f | Stock: %d | Categoría: %s%n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), p.getCategoria().getNombre());
        }

        System.out.print("Ingrese el ID del producto a modificar: ");
        Long id = parsearLong(scanner.nextLine().trim());
        if (id == null) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Producto> opt = productoRepo.buscarPorId(id);
        if (opt.isEmpty() || opt.get().isEliminado()) {
            System.out.println("Error: no se encontró un producto activo con el ID " + id + ".");
            return;
        }

        Producto producto = opt.get();
        System.out.printf("Valores actuales → Nombre: %s | Precio: $%.2f | Stock: %d%n",
                producto.getNombre(), producto.getPrecio(), producto.getStock());
        System.out.println("(Dejar en blanco para conservar el valor actual)");

        System.out.print("Nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();
        if (!nuevoNombre.isEmpty()) {
            producto.setNombre(nuevoNombre);
        }

        System.out.print("Nuevo precio (mayor a 0): ");
        String precioStr = scanner.nextLine().trim();
        if (!precioStr.isEmpty()) {
            Double nuevoPrecio = parsearDouble(precioStr);
            if (nuevoPrecio == null || nuevoPrecio <= 0) {
                System.out.println("Error: el precio debe ser mayor a 0. No se actualizó el precio.");
            } else {
                producto.setPrecio(nuevoPrecio);
            }
        }

        System.out.print("Nuevo stock (mayor o igual a 0): ");
        String stockStr = scanner.nextLine().trim();
        if (!stockStr.isEmpty()) {
            Integer nuevoStock = parsearInt(stockStr);
            if (nuevoStock == null || nuevoStock < 0) {
                System.out.println("Error: el stock no puede ser negativo. No se actualizó el stock.");
            } else {
                producto.setStock(nuevoStock);
            }
        }

        productoRepo.guardar(producto);
        System.out.println("Producto actualizado correctamente.");
    }

    /** Baja logica de producto */
    private static void bajaProducto() {
        System.out.println("\n--- BAJA DE PRODUCTO ---");

        System.out.print("Ingrese el ID del producto a dar de baja: ");
        Long id = parsearLong(scanner.nextLine().trim());
        if (id == null) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Producto> opt = productoRepo.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("Error: no existe ningún producto con el ID " + id + ".");
            return;
        }

        Producto producto = opt.get();
        if (producto.isEliminado()) {
            System.out.println("Error: el producto \"" + producto.getNombre() + "\" ya está dado de baja.");
            return;
        }

        boolean resultado = productoRepo.eliminarLogico(id);
        if (resultado) {
            System.out.println("Producto \"" + producto.getNombre() + "\" dado de baja exitosamente.");
        } else {
            System.out.println("Error: no se pudo dar de baja el producto.");
        }
    }

    /** Listado de productos activos */
    private static void listarProductos() {
        System.out.println("\n--- LISTADO DE PRODUCTOS ACTIVOS ---");

        List<Producto> activos = productoRepo.listarActivos();
        if (activos.isEmpty()) {
            System.out.println("No hay productos activos.");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-8s %-20s%n",
                "ID", "Nombre", "Precio", "Stock", "Categoría");
        System.out.println("-".repeat(65));
        for (Producto p : activos) {
            System.out.printf("%-5d %-20s $%-9.2f %-8d %-20s%n",
                    p.getId(), p.getNombre(), p.getPrecio(),
                    p.getStock(), p.getCategoria().getNombre());
        }
    }


    //  Submenu de reportes
    private static void menuReportes() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Productos por categoría");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> productosPorCategoria();
                case "0" -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    /** Consulta JPQL - productos por categoría */
    private static void productosPorCategoria() {
        System.out.println("\n--- PRODUCTOS POR CATEGORÍA ---");

        List<Categoria> categorias = categoriaRepo.listarActivos();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorías activas.");
            return;
        }

        System.out.println("Categorías disponibles:");
        for (Categoria c : categorias) {
            System.out.println("  [" + c.getId() + "] " + c.getNombre());
        }

        System.out.print("Seleccione el ID de la categoría: ");
        Long catId = parsearLong(scanner.nextLine().trim());
        if (catId == null) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Categoria> optCat = categoriaRepo.buscarPorId(catId);
        if (optCat.isEmpty() || optCat.get().isEliminado()) {
            System.out.println("Error: categoría no encontrada.");
            return;
        }

        List<Producto> productos = productoRepo.buscarPorCategoria(catId);

        if (productos.isEmpty()) {
            System.out.println("La categoría \"" + optCat.get().getNombre()
                    + "\" no tiene productos activos.");
            return;
        }

        System.out.println("\nProductos activos en \"" + optCat.get().getNombre() + "\":");
        System.out.printf("%-5s %-20s %-10s %-8s%n", "ID", "Nombre", "Precio", "Stock");
        System.out.println("-".repeat(45));
        for (Producto p : productos) {
            System.out.printf("%-5d %-20s $%-9.2f %-8d%n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock());
        }
    }


    //  Utilidades de parseo
    private static Long parsearLong(String valor) {
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double parsearDouble(String valor) {
        try {
            return Double.parseDouble(valor.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parsearInt(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
