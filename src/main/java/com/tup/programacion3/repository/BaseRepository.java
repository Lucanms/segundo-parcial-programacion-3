package com.tup.programacion3.repository;

import com.tup.programacion3.entities.Base;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Base> {

    private final Class<T> clazz;
    private final EntityManagerFactory emf;

    /**
     * Constructor que recibe la clase de la entidad y obtiene el EMF desde la unidad de persistencia.
     *
     * @param clazz Clase de la entidad gestionada por este repositorio
     */
    public BaseRepository(Class<T> clazz) {
        this.clazz = clazz;
        this.emf = Persistence.createEntityManagerFactory("miUnidad");
    }

    /**
     * Persiste o actualiza la entidad usando merge().
     * Abre y cierra su propio EntityManager y maneja la transacción.
     *
     * @param entity Entidad a guardar o actualizar
     * @return La entidad guardada (con ID generado si es nueva)
     */
    public T guardar(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T result = em.merge(entity);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar la entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Busca una entidad por su ID usando find().
     *
     * @param id ID de la entidad a buscar
     * @return Optional con la entidad si existe, Optional.empty() si no
     */
    public Optional<T> buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            T entity = em.find(clazz, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los registros activos (eliminado = false) usando JPQL.
     *
     * @return Lista de entidades activas
     */
    public List<T> listarActivos() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e WHERE e.eliminado = false";
            return em.createQuery(jpql, clazz).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Realiza una baja lógica estableciendo eliminado = true.
     * No elimina el registro de la base de datos.
     *
     * @param id ID de la entidad a dar de baja
     * @return true si se encontró y dio de baja, false si no existe
     */
    public boolean eliminarLogico(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            T entity = em.find(clazz, id);
            if (entity == null) {
                return false;
            }
            em.getTransaction().begin();
            entity.setEliminado(true);
            em.merge(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar la entidad: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}