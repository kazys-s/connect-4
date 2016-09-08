package com.connect4.domain.inmemory;

import com.connect4.domain.Color;
import com.connect4.domain.Player;
import com.connect4.domain.Slot;
import com.connect4.domain.exceptions.ColorAlreadyTakenException;
import com.connect4.domain.exceptions.NoSlotAvailableException;
import com.connect4.domain.exceptions.PlayerAlreadyJoinedException;
import lombok.NonNull;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.connect4.domain.Slot.Predicates.sameColor;
import static com.connect4.domain.Slot.Predicates.samePlayer;

class SlotAllocator {

    private final CopyOnWriteArrayList<Slot> slots = new CopyOnWriteArrayList<>();

    synchronized Slot allocate(@NonNull Player player, @NonNull Color color) {
        if (!isFreeSlotAvailable()) {
            throw new NoSlotAvailableException();
        }

        if (slotWith(samePlayer(player)).isPresent()) {
            throw new PlayerAlreadyJoinedException();
        }

        if (slotWith(sameColor(color)).isPresent()) {
            throw new ColorAlreadyTakenException();
        }

        Slot slot = new Slot(player, color);
        slots.add(slot);
        return slot;
    }


    boolean isFreeSlotAvailable() {
        return slots.size() < 2;
    }

    Optional<Slot> slotWith(Predicate<Slot> predicate) {
        return slotsWith(predicate).findFirst();
    }

    Stream<Slot> slotsWith(Predicate<Slot> predicate) {
        return slots.stream().filter(predicate);
    }
}
