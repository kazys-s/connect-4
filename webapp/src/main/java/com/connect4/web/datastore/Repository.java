package com.connect4.web.datastore;

import com.connect4.domain.WithId;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Repository<T extends WithId> {
    T get(int id) throws ResourceNotFoundException;

    T create(Function<Integer, T> entityFactory);

    Stream<T> find(Predicate<T> entity);

    default Stream<T> findAll() {
        return find((any) -> true);
    }
}
