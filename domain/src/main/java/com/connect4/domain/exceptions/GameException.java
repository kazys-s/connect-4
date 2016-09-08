package com.connect4.domain.exceptions;

public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }
}
