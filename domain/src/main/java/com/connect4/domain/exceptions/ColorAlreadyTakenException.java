package com.connect4.domain.exceptions;

public class ColorAlreadyTakenException extends GameException {
    public ColorAlreadyTakenException() {
        super("Color already taken");
    }
}
