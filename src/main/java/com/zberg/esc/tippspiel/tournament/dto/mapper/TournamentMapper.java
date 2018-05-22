package com.zberg.esc.tippspiel.tournament.dto.mapper;

import com.zberg.esc.tippspiel.tournament.db.entities.Game;
import com.zberg.esc.tippspiel.tournament.db.entities.Scorer;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import com.zberg.esc.tippspiel.tournament.dto.GameDto;
import com.zberg.esc.tippspiel.tournament.dto.TournamentDto;

import java.util.Comparator;
import java.util.stream.Collectors;

public class TournamentMapper {

    public TournamentDto toDto(final Tournament tournament) {
        final TournamentDto dto = new TournamentDto();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setChampionId(tournament.getChampion() != null ? tournament.getChampion().getId() : null);
        dto.setScorers(tournament.getTopScorers() == null ? null :
                tournament.getTopScorers().stream().map(Scorer::getName).collect(Collectors.toList())
        );
        dto.setScoreRange(tournament.getScoreRange());
        dto.setGames(tournament.getGames().stream()//
                .sorted(Comparator.comparingInt(Game::getOrder))
                .map(g -> {
                    final GameDto gameDto = new GameDto();
                    gameDto.setId(g.getId());
                    gameDto.setScore1(g.getScore1() == -1 ? null : g.getScore1());
                    gameDto.setScore2(g.getScore2() == -1 ? null : g.getScore2());
                    gameDto.setTeam1(g.getTeam1().getName());
                    gameDto.setTeam2(g.getTeam2().getName());
                    return gameDto;
                }).collect(Collectors.toList()));
        return dto;
    }
}
