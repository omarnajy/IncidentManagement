package dao;

import java.util.*;

public interface GenericDAO<T> {

    List<T> findAll();
    T findById(Long id);
    /**
     * Add the entity and return the generated database id (Long).
     */
    Long add(T entity);
    void update(T entity);
    void delete(Long id);
}
