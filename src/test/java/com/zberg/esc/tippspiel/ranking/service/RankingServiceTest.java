package com.zberg.esc.tippspiel.ranking.service;

import com.zberg.esc.tippspiel.ranking.dto.PlayerDto;
import com.zberg.esc.tippspiel.ranking.dto.Ranking;
import com.zberg.esc.tippspiel.tournament.TournamentService;
import com.zberg.esc.tippspiel.tournament.db.entities.Player;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RankingServiceTest {
    @Mock
    private ScoreCalculator scoreCalculator;
    @Mock
    private TournamentService tournamentService;
    @InjectMocks
    private RankingService testee;

    @Test
    public void getRankingForTournament_twoPlayerDifferentScores_normalRanking() {
        // arrange
        final Tournament tournament = new Tournament();
        tournament.setId("tid");
        final Player player1 = createPlayer(tournament, 1);
        when(scoreCalculator.calculateScore(player1)).thenReturn(new Score(player1, 55));
        final Player player2 = createPlayer(tournament, 2);
        when(scoreCalculator.calculateScore(player2)).thenReturn(new Score(player2, 11));
        when(tournamentService.get()).thenReturn(tournament);
        // act
        final List<Ranking> result = testee.getRankingForTournament();
        // assert
        assertEquals(1, result.get(0).getPlayer().getId());
        assertEquals(2, result.get(1).getPlayer().getId());
        assertPlayerValuesMapped(result.get(0).getPlayer(), 1);
        assertPlayerValuesMapped(result.get(1).getPlayer(), 2);
    }

    @Test
    public void getRankingForTournament_allPlayerSameScore_sameRankings() {
        // arrange
        final Tournament tournament = new Tournament();
        tournament.setId("tid");
        final Player player1 = createPlayer(tournament, 1);
        when(scoreCalculator.calculateScore(player1)).thenReturn(new Score(player1, 55));
        final Player player2 = createPlayer(tournament, 2);
        when(scoreCalculator.calculateScore(player2)).thenReturn(new Score(player2, 55));
        when(tournamentService.get()).thenReturn(tournament);
        // act
        final List<Ranking> result = testee.getRankingForTournament();
        // assert
        assertEquals(1, result.get(0).getRanking());
        assertEquals(1, result.get(1).getRanking());
    }

    @Test
    public void getRankingForTournament_4PlayersSameScoreFifthDifferent_rankingJumpedTo5() {
        // arrange
        final Tournament tournament = new Tournament();
        tournament.setId("tid");
        final Player player1 = createPlayer(tournament, 1);
        when(scoreCalculator.calculateScore(player1)).thenReturn(new Score(player1, 55));
        final Player player2 = createPlayer(tournament, 2);
        when(scoreCalculator.calculateScore(player2)).thenReturn(new Score(player2, 55));
        final Player player3 = createPlayer(tournament, 3);
        when(scoreCalculator.calculateScore(player3)).thenReturn(new Score(player3, 55));
        final Player player4 = createPlayer(tournament, 4);
        when(scoreCalculator.calculateScore(player4)).thenReturn(new Score(player4, 55));
        final Player player5 = createPlayer(tournament, 5);
        when(scoreCalculator.calculateScore(player5)).thenReturn(new Score(player5, 22));
        when(tournamentService.get()).thenReturn(tournament);
        // act
        final List<Ranking> result = testee.getRankingForTournament();
        // assert
        assertEquals(1, result.get(0).getRanking());
        assertEquals(1, result.get(1).getRanking());
        assertEquals(1, result.get(2).getRanking());
        assertEquals(1, result.get(3).getRanking());
        assertEquals(5, result.get(4).getRanking());
    }

    private void assertPlayerValuesMapped(PlayerDto player, int id) {
        assertEquals("firstname" + id, player.getFirstname());
        assertEquals("lastname" + id, player.getLastname());
        assertEquals("street" + id, player.getStreet());
        assertEquals("city" + id, player.getCity());
        assertEquals("zip" + id, player.getZip());
    }

    private Player createPlayer(Tournament tournament, long id) {
        final Player player1 = new Player();
        player1.setId(id);
        player1.setFirstname("firstname" + id);
        player1.setLastname("lastname" + id);
        player1.setCity("city" + id);
        player1.setStreet("street" + id);
        player1.setZip("zip" + id);
        tournament.getPlayers().add(player1);
        return player1;
    }

}