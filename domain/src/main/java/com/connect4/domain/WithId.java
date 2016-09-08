package com.connect4.domain;

public interface WithId {
    Integer getId();

    default boolean idEquals(WithId other) {
        return getId().equals(other.getId());
    }

}
