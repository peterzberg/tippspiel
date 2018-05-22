package com.zberg.esc.tippspiel.tournament;

public class TournamentNotFoundException extends RuntimeException {

    TournamentNotFoundException(String message) {
        super(message);
    }
}
