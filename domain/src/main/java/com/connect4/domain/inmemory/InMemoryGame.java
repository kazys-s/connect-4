package com.connect4.domain.inmemory;

import com.connect4.domain.*;
import com.connect4.domain.exceptions.GameIsInProgressException;
import com.connect4.domain.exceptions.GameIsNotInProgressException;
import com.connect4.domain.exceptions.IllegalMoveException;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Predicate;

import static com.connect4.domain.Game.Status.*;
import static com.connect4.domain.Slot.Predicates.sameColor;
import static java.util.stream.Collectors.toList;

public class InMemoryGame implements Game {
    private final Integer id;
    private final SlotAllocator slotAllocator;
    private final Board board;

    private volatile Slot currentPlayer;

    public InMemoryGame(int id) {
        this(id, new SlotAllocator(), new Board());
    }

    public InMemoryGame(int id, @NonNull SlotAllocator slotAllocator, @NonNull Board board) {
        this.id = id;
        this.slotAllocator = slotAllocator;
        this.board = board;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Game assignSlot(@NonNull Player player, @NonNull Color color) {
        if (getStatus() != WAITING_FOR_PLAYERS) {
            throw new GameIsInProgressException();
        }

        currentPlayer = slotAllocator.allocate(player, color);
        return this;
    }

    @Override
    public synchronized Game dropDisc(@NonNull Player player, int column) {
        if (getStatus() != IN_PROGRESS) {
            throw new GameIsNotInProgressException();
        }

        if (!currentPlayer.getPlayer().idEquals(player)) {
            throw new IllegalMoveException("It's not your move");
        }

        board.dropDisc(column, currentPlayer.getColor());
        changePlayer();
        return this;
    }

    private void changePlayer() {
        currentPlayer = slotWith((s) -> s != currentPlayer);
    }

    private Slot slotWith(Predicate<Slot> slotPredicate) {
        return slotAllocator.slotWith(slotPredicate).get();
    }

    @Override
    public GameState getState() {
        return GameState.builder()
                .boardSnapshot(board.getBoardSnapshot())
                .allocatedSlots(slotAllocator.slotsWith((s) -> true).collect(toList()))
                .currentPlayer(getStatus() == IN_PROGRESS ? Optional.of(currentPlayer.getPlayer()) : Optional.<Player>empty())
                .status(getStatus())
                .result(getResult())
                .build();
    }

    public Status getStatus() {
        if (slotAllocator.isFreeSlotAvailable()) {
            return WAITING_FOR_PLAYERS;
        } else if (board.getOutcome().isPresent()) {
            return FINISHED;
        } else {
            return IN_PROGRESS;
        }
    }

    public Optional<Outcome<Player>> getResult() {
        if (board.getOutcome().isPresent()) {
            Outcome<Color> outcome = board.getOutcome().get();
            Optional<Color> winningColor = outcome.getWinner();
            return Optional.of(new Outcome<>(outcome.getType(), toWinningPlayer(winningColor)));
        } else {
            return Optional.empty();
        }
    }

    private Optional<Player> toWinningPlayer(Optional<Color> winningColor) {
        return winningColor.isPresent() ?
                Optional.of(slotWith(sameColor(winningColor.get())).getPlayer()) :
                Optional.empty();
    }
}
