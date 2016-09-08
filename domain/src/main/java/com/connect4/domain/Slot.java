package com.connect4.domain;

import lombok.Data;
import lombok.NonNull;

import java.util.function.Predicate;

@Data
public class Slot {
    private final Player player;
    private final Color color;

    public static class Predicates {
        public static Predicate<Slot> sameColor(@NonNull Color color) {
            return s -> s.getColor().equals(color);
        }

        public static Predicate<Slot> samePlayer(@NonNull Player player) {
            return s -> s.getPlayer().idEquals(player);
        }
    }
}
