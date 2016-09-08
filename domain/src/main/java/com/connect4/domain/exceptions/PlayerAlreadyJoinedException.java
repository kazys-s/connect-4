package com.connect4.domain.exceptions;

public class PlayerAlreadyJoinedException extends GameException {
    public PlayerAlreadyJoinedException() {
        super("Player already joined");
    }
}
