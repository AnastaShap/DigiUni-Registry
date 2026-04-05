package ua.university.repository;

import java.util.*;

// Базова реалізація для всіх репозиторіїв !!!
public abstract class InMemoryRepository<T, ID> implements IRepository<T, ID> {
    protected final Map<ID, T> storage = new HashMap<>();

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

    }

    @Override
    public void deleteById(ID id) {
        storage.remove(id);
    }
}
