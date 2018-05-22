package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.tournament.db.entities.Player;

class Score {
    private final Player player;
    private final int score;

    Score(Player player, int score) {
        this.player = player;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Player getPlayer() {
        return player;
    }
}
