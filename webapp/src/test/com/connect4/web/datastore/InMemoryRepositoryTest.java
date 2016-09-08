package com.connect4.web.datastore;

import com.connect4.domain.WithId;
import lombok.Data;
import lombok.experimental.Wither;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

public class InMemoryRepositoryTest {

    private final InMemoryRepository<TestEntity> repository = new InMemoryRepository<>();

    @Test(expected = ResourceNotFoundException.class)
    public void unknownIdShouldThrowEntityNotFoundException() {
        repository.get(0);
    }

    @Test
    public void createdEntityShouldBeRetrievable() {
        TestEntity entity = repository.create(entityWithName("name"));
        assertThat(repository.get(entity.getId())).isSameAs(entity);
    }

    @Test
    public void findShouldApplyPredicate() {
        repository.create(entityWithName("name1"));
        repository.create(entityWithName("name2"));
        repository.create(entityWithName("name3"));

        Stream<TestEntity> results = repository.find((e) -> e.getName().equals("name2"));
        assertThat(results.toArray()).containsOnly(new TestEntity(2, "name2"));
    }

    @Test
    public void filteredResultsShouldBeSortedById() {
        repository.create(entityWithName("name1"));
        repository.create(entityWithName("name2"));
        repository.create(entityWithName("name3"));

        Stream<TestEntity> results = repository.find((e) -> true);
        assertThat(results.toArray()).containsOnly(
                new TestEntity(1, "name1"),
                new TestEntity(2, "name2"),
                new TestEntity(3, "name3")
        );
    }

    private Function<Integer, TestEntity> entityWithName(String name) {
        return (id) -> new TestEntity(id, name);
    }

    @Data
    @Wither
    public static class TestEntity implements WithId {
        private final Integer id;
        private final String name;
    }
}