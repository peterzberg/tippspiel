package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.tournament.db.entities.*;
import org.springframework.stereotype.Service;

import java.util.*;

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
                .stream().anyMatch(s -> nameMatches(s, topScorer));
    }

    private boolean nameMatches(Scorer tournamentScorer, String topScorer) {
        final String tournamentScorerName = tournamentScorer.getName();
        if (tournamentScorerName.contains(" ")) {
            final String[] split = tournamentScorerName.split(" ");
            final List<String> combinations = determineAllNameCombinations(split);
            return combinations.stream().anyMatch(c -> c.equalsIgnoreCase(topScorer));
        } else {
            return topScorer.equals(tournamentScorerName);
        }
    }


    private List<String> determineAllNameCombinations(String[] arr) {
        final List<String> result = new ArrayList<>();
        determineAllNameCombinations(arr, 0, result);
        return result;
    }

    private void determineAllNameCombinations(String[] arr, int index, final List<String> result) {
        if (index >= arr.length - 1) { //if we are at the last element - nothing left to permute
            final StringBuilder permutation = new StringBuilder();
            for (int i = 0; i < arr.length - 1; i++) {
                permutation.append(arr[i]).append(" ");
            }
            if (arr.length > 0) {
                permutation.append(arr[arr.length - 1]);
            }
            result.add(permutation.toString());
            return;
        }

        for (int i = index; i < arr.length; i++) { //For each index in the sub array arr[index...end]

            //Swap the elements at indices index and i
            String t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;

            //Recurse on the sub array arr[index+1...end]
            determineAllNameCombinations(arr, index + 1, result);

            //Swap the elements back
            t = arr[index];
            arr[index] = arr[i];
            arr[i] = t;
        }
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
