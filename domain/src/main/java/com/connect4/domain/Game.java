package com.connect4.domain;

import lombok.Data;
import lombok.experimental.Builder;
import lombok.experimental.Wither;

import java.util.Collection;
import java.util.Optional;

public interface Game extends WithId {
    enum Status {
        WAITING_FOR_PLAYERS, IN_PROGRESS, FINISHED
    }

    Game assignSlot(Player player, Color color);

    Game dropDisc(Player player, int column);

    GameState getState();

    @Data
    @Builder
    @Wither
    class GameState {
        private final Collection<Slot> allocatedSlots;
        private final Color[][] boardSnapshot;
        private final Optional<Player> currentPlayer;
        private final Status status;
        private final Optional<Outcome<Player>> result;
    }
}
