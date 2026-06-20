package com.tup.programacion3.repository;
import com.tup.programacion3.entities.Categoria;

public class CategoriaRepository extends BaseRepository<Categoria> {

    /**
     * Constructor que pasa Categoria.class al repositorio base.
     */
    public CategoriaRepository() {
        super(Categoria.class);
    }
}
