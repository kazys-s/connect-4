package com.connect4.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.Optional;

@Data
public class Outcome<T> {
    public enum Type {DRAW, WIN}

    @NonNull
    private final Type type;

    @NonNull
    private final Optional<T> winner;

    public static <T> Outcome<T> draw() {
        return new Outcome<>(Type.DRAW, Optional.empty());
    }

    public static <T> Outcome<T> win(T winner) {
        return new Outcome<>(Type.WIN, Optional.of(winner));
    }
}
