package com.zberg.esc.tippspiel.tournament;

public class PlayerNotFoundException extends RuntimeException {
    PlayerNotFoundException(String message) {
        super(message);
    }
}
