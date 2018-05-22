package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.tournament.db.entities.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
class ScoreCalculator {

    Score calculateScore(final Player player) {
        final List<Bet> bets = player.getBets();
        int score = bets.stream().filter(b -> b.getGame().isPlayed())//
                .map(this::toScore)//
                .mapToInt(Integer::intValue).sum();

        if (isTopScorerGuessedCorrectly(player)) {
            score += 10;
        }
        if (isTotalScoreRangeGuessedCorrectly(player)) {
            score += 10;
        }

        return new Score(player, score);

    }

    private boolean isTotalScoreRangeGuessedCorrectly(Player player) {
        final Tournament tournament = player.getTournament();
        return tournament.getScoreRange() != null && tournament.getScoreRange().equals(player.getScoreRange());

    }

    private boolean isTopScorerGuessedCorrectly(final Player player) {
        final Tournament tournament = player.getTournament();
        final Set<Scorer> tournameTopScorer = tournament.getTopScorers();
        final String topScorer = player.getGuessedTopScorer();

        return Optional.ofNullable(tournameTopScorer).orElse(new HashSet<>())
                .stream().anyMatch(ts -> ts.getName().equals(topScorer));
    }

    private int toScore(Bet bet) {
        final Game game = bet.getGame();
        if (isFullMatch(bet, game)) {
            return 6;
        } else if (endingCorrect(bet, game)) {
            return 3;
        }

        return 0;
    }

    private boolean endingCorrect(Bet bet, Game game) {
        final int betEnding = bet.getScore1() == bet.getScore2() ? 0 : bet.getScore1() < bet.getScore2() ? -1 : 1;
        final int gameEnding = game.getScore1() == game.getScore2() ? 0 : game.getScore1() < game.getScore2() ? -1 : 1;
        return betEnding == gameEnding;

    }

    private boolean isFullMatch(Bet bet, Game game) {
        return bet.getScore1() == game.getScore1() && bet.getScore2() == game.getScore2();
    }

}
