package com.connect4.domain.exceptions;

public class NoSlotAvailableException extends GameException {
    public NoSlotAvailableException() {
        super("No slot available");
    }
}
