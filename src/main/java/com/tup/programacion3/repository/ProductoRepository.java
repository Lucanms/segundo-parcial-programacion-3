package com.tup.programacion3.repository;

import com.tup.programacion3.entities.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Repositorio específico para la entidad Producto.
 * Extiende BaseRepository y agrega la consulta JPQL para filtrar por categoría.
 */
public class ProductoRepository extends BaseRepository<Producto> {

    private final EntityManagerFactory emf;

    /**
     * Constructor que pasa Producto.class al repositorio base
     * y obtiene el EMF para los métodos propios de este repositorio.
     */
    public ProductoRepository() {
        super(Producto.class);
        this.emf = Persistence.createEntityManagerFactory("miUnidad");
    }

    /**
     * Busca todos los productos activos que pertenecen a una categoría específica.
     *
     * La consulta JPQL filtra por:
     *   - p.categoria.id = :categoriaId  → solo productos de la categoría indicada
     *   - p.eliminado = false            → solo productos que no fueron dados de baja lógica
     *
     * Se usa TypedQuery<Producto> para que JPA mapee el resultado directamente
     * sin necesidad de casteos manuales.
     *
     * @param categoriaId ID de la categoría por la que se desea filtrar
     * @return Lista de productos activos pertenecientes a esa categoría
     */
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL: selecciona productos cuya categoría tenga el id dado y no estén eliminados
            String jpql = "SELECT p FROM Producto p " +
                    "WHERE p.categoria.id = :categoriaId " +
                    "AND p.eliminado = false";

            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("categoriaId", categoriaId);

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}