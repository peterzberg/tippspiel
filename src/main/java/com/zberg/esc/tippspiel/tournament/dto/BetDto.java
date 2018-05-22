package com.zberg.esc.tippspiel.tournament.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BetDto {
    private Long id;
    @NotNull
    private Long gameId;
    private String team1;
    private String team2;
    @NotNull
    @Min(0)
    private Integer score1;
    @NotNull
    @Min(0)
    private Integer score2;
}
