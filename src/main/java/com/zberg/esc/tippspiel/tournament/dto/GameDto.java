package com.zberg.esc.tippspiel.tournament.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class GameDto {
    private Long id;
    private String team1;
    private String team2;
    @Min(0)
    private Integer score1;
    @Min(0)
    private Integer score2;

    public boolean isPlayed() {
        return score1 != null && score2 != null;
    }
}
