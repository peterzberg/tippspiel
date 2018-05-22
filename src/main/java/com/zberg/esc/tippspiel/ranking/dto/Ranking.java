package com.zberg.esc.tippspiel.ranking.dto;

import lombok.Data;

@Data
public class Ranking {
    private int ranking;
    private PlayerDto player;
    private int score;
}
