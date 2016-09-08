package com.connect4.web.datastore;

import com.connect4.domain.WithId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class InMemoryRepository<T extends WithId> implements Repository<T> {

    private final AtomicInteger idSequence = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, T> storage = new ConcurrentHashMap<>();

    @Override
    public T get(int id) {
        T entity = storage.get(id);

        if (entity == null) {
            throw new ResourceNotFoundException(id);
        }

        return entity;
    }

    @Override
    public T create(Function<Integer, T> entityFactory) {
        int id = idSequence.incrementAndGet();
        storage.put(id, entityFactory.apply(id));
        return storage.get(id);
    }

    @Override
    public Stream<T> find(Predicate<T> entity) {
        return storage.values().stream()
                .filter(entity)
                .sorted((e1, e2) -> e1.getId().compareTo(e2.getId()));
    }
}
