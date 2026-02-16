package ua.university.repository;

import java.util.List;
import java.util.Optional;

// FOR FUTURE USAGES (Generics)
// Generalization via Repository<T, ID> is planned in the next stages.
public interface IRepository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    void save(T entity);

    void deleteById(ID id);

    //void updateById(ID id);

    //void show(T entity);
}