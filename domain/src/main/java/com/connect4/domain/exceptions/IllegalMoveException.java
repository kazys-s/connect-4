package com.connect4.domain.exceptions;

public class IllegalMoveException extends GameException {
    public IllegalMoveException(String message) {
        super(message);
    }
}
