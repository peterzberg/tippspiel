package com.zberg.esc.tippspiel.tournament.dto;

import com.zberg.esc.tippspiel.tournament.db.entities.ScoreRange;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TournamentDto {
    private String id;
    private String name;
    private List<String> scorers = new ArrayList<>();
    private Long championId;
    private ScoreRange scoreRange;
    private List<GameDto> games;
}
