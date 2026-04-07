package ua.university.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Базова реалізація для всіх репозиторіїв !!!
public abstract class InMemoryRepository<T, ID> implements IRepository<T, ID> {
    protected final Map<ID, T> storage = new ConcurrentHashMap<>();

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(T entity) {
        // Тут можна додати логіку отримання ID через рефлексію або інтерфейс HasId
        // Метод save залишається абстрактним або реалізується в нащадках,
        // бо кожен об'єкт (Student, Teacher, Dept) має свій спосіб отримання ID
    }

    @Override
    public void deleteById(ID id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(ID id) {
        return storage.containsKey(id);
    }
}
