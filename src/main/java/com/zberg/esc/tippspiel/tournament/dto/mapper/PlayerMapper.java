package com.zberg.esc.tippspiel.tournament.dto.mapper;

import com.zberg.esc.tippspiel.tournament.db.entities.Bet;
import com.zberg.esc.tippspiel.tournament.db.entities.Player;
import com.zberg.esc.tippspiel.tournament.dto.BetDto;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMapper {


    public PlayerDto toDto(final Player player) {
        final PlayerDto playerDto = new PlayerDto();
        playerDto.setTournamentId(player.getTournament().getId());
        playerDto.setId(player.getId());
        playerDto.setFirstname(player.getFirstname());
        playerDto.setLastname(player.getLastname());
        playerDto.setStreet(player.getStreet());
        playerDto.setZip(player.getZip());
        playerDto.setCity(player.getCity());
        playerDto.setGuessedChampion(player.getGuessedChampion().getId());
        playerDto.setGuessedTopScorer(player.getGuessedTopScorer());
        playerDto.setScoreRange(player.getScoreRange());
        playerDto.setBets(toBetDto(player.getBets()));
        return playerDto;

    }

    private List<BetDto> toBetDto(List<Bet> bets) {
        return bets.stream()//
                .sorted(Comparator.comparingInt(l -> l.getGame().getOrder()))
                .map(b -> {
                    final BetDto result = new BetDto();
                    result.setId(b.getId());
                    result.setGameId(b.getGame().getId());
                    result.setScore1(b.getScore1());
                    result.setScore2(b.getScore2());
                    result.setTeam1(b.getGame().getTeam1().getName());
                    result.setTeam2(b.getGame().getTeam2().getName());
                    return result;
                }).collect(Collectors.toList());
    }
}
