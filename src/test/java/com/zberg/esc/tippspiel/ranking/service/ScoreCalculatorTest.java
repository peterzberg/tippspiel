package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.tournament.db.entities.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ScoreCalculatorTest {

    private ScoreCalculator testee = new ScoreCalculator();
    private Player player;
    private Tournament tournament;

    @Before
    public void setUp() {
        player = new Player();
        tournament = new Tournament();
        player.setTournament(tournament);
        player.setBets(new ArrayList<>());
    }


    @Test
    public void calculateScore_allWrong_0() {
        // arrange
        final Bet bet = createBet(2, 1);
        final Game game = createGame(1, 2);
        bet.setGame(game);
        player.getBets().add(bet);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(0, result.getScore());
    }

    @Test
    public void calculateScore_allRight_6() {
        // arrange
        final Bet bet = createBet(2, 1);
        final Game game = createGame(2, 1);
        bet.setGame(game);
        player.getBets().add(bet);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(6, result.getScore());
    }

    @Test
    public void calculateScore_correctWinner_3() {
        // arrange
        final Bet bet = createBet(3, 1);
        final Game game = createGame(2, 1);
        bet.setGame(game);
        player.getBets().add(bet);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(3, result.getScore());
    }

    @Test
    public void calculateScore_guessedTopScorer_10() {
        // arrange
        player.setGuessedTopScorer("wu");
        final Set<Scorer> scorers = new HashSet<>();
        final Scorer scorer = new Scorer();
        scorer.setName("wu");
        scorers.add(scorer);
        tournament.setTopScorers(scorers);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(10, result.getScore());
    }

    @Test
    public void calculateScore_TwoNamesScoreMatchesCombination_10() {
        // arrange
        player.setGuessedTopScorer("Peter Zberg");
        final Set<Scorer> scorers = new HashSet<>();
        final Scorer scorer = new Scorer();
        scorer.setName("Zberg Peter");
        scorers.add(scorer);
        tournament.setTopScorers(scorers);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(10, result.getScore());
    }

    @Test
    public void calculateScore_ThreeNamesscoreMatchesCombination_10() {
        // arrange
        player.setGuessedTopScorer("Peter Wu Zberg");
        final Set<Scorer> scorers = new HashSet<>();
        final Scorer scorer = new Scorer();
        scorer.setName("Wu Zberg Peter");
        scorers.add(scorer);
        tournament.setTopScorers(scorers);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(10, result.getScore());
    }

    @Test
    public void calculateScore_ThreeNamesscoreMatchesCombinationIgnoreCase_10() {
        // arrange
        player.setGuessedTopScorer("peter WU zberg");
        final Set<Scorer> scorers = new HashSet<>();
        final Scorer scorer = new Scorer();
        scorer.setName("WU ZBERG PETER");
        scorers.add(scorer);
        tournament.setTopScorers(scorers);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(10, result.getScore());
    }

    @Test
    public void calculateScore_champtionGuessedCorrectly_25() {
        // arrange
        final Team guessedChamption = new Team();
        guessedChamption.setId(15L);
        player.setGuessedChampion(guessedChamption);
        final Team realChamption = new Team();
        realChamption.setId(15L);
        tournament.setChampion(realChamption);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(25, result.getScore());
    }

    @Test
    public void calculateScore_guessedScoreRange_10() {
        // arrange
        tournament.setScoreRange(ScoreRange.R_111_120);
        player.setScoreRange(ScoreRange.R_111_120);
        // act
        final Score result = testee.calculateScore(player);
        // assert
        assertEquals(10, result.getScore());
    }

    private Bet createBet(int score1, int score2) {
        final Bet result = new Bet();
        result.setScore1(score1);
        result.setScore2(score2);
        return result;
    }

    private Game createGame(int score1, int score2) {
        final Game result = new Game();
        result.setScore1(score1);
        result.setScore2(score2);
        return result;
    }


}