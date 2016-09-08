package com.connect4.domain.exceptions;

public class GameIsInProgressException extends GameException {
    public GameIsInProgressException() {
        super("Game is in progress");
    }
}
