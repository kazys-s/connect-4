package com.connect4.domain.exceptions;

public class GameIsNotInProgressException extends GameException {
    public GameIsNotInProgressException() {
        super("Game is not in progress");
    }
}
