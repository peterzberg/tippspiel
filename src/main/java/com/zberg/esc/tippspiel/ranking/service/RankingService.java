package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.ranking.dto.PlayerDto;
import com.zberg.esc.tippspiel.ranking.dto.Ranking;
import com.zberg.esc.tippspiel.tournament.TournamentService;
import com.zberg.esc.tippspiel.tournament.db.entities.Player;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    private final TournamentService tournamentService;
    private final ScoreCalculator scoreCalculator;

    @Autowired
    public RankingService(final TournamentService tournamentService, final ScoreCalculator scoreCalculator) {
        this.tournamentService = tournamentService;
        this.scoreCalculator = scoreCalculator;
    }

    public List<Ranking> getRankingForTournament() {
        final Tournament tournament = tournamentService.get();
        final List<Player> players = tournament.getPlayers();
        final List<Score> scores = new ArrayList<>();
        players.forEach(player -> {
            scores.add(scoreCalculator.calculateScore(player));
        });
        scores.sort(new ScoreComparator());
        return createRanking(scores);
    }

    private List<Ranking> createRanking(List<Score> scores) {
        int rankingPos = 1;
        final List<Ranking> rankings = new ArrayList<>(scores.size());
        Ranking lastRanking = null;
        for (Score currentScore : scores) {
            final Ranking currentRanking = createRankingForPlayer(currentScore);
            if (!isSameScore(lastRanking, currentScore)) {
                currentRanking.setRanking(rankingPos);
            } else {
                currentRanking.setRanking(lastRanking.getRanking());
            }
            lastRanking = currentRanking;
            rankings.add(currentRanking);
            rankingPos++;
        }
        return rankings;
    }

    private Ranking createRankingForPlayer(Score score) {
        final Ranking ranking = new Ranking();
        ranking.setPlayer(toPlayerDto(score.getPlayer()));
        ranking.setScore(score.getScore());
        return ranking;
    }

    private boolean isSameScore(Ranking lastRanking, Score currentScore) {
        return lastRanking != null && lastRanking.getScore() == currentScore.getScore();
    }

    private PlayerDto toPlayerDto(Player player) {
        final PlayerDto result = new PlayerDto();
        result.setId(player.getId());
        result.setCity(player.getCity());
        result.setZip(player.getZip());
        result.setFirstname(player.getFirstname());
        result.setLastname(player.getLastname());
        result.setStreet(player.getStreet());
        return result;
    }

}
