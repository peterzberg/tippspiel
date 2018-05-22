package com.zberg.esc.tippspiel.tournament;

import com.zberg.esc.tippspiel.tournament.db.PlayerRepository;
import com.zberg.esc.tippspiel.tournament.db.ScorerRepository;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.db.TournamentRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.Game;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import com.zberg.esc.tippspiel.tournament.dto.GameDto;
import com.zberg.esc.tippspiel.tournament.dto.TournamentDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TournamentServiceTest {
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private ScorerRepository scorerRepository;
    @Mock
    private PlayerService playerService;
    @InjectMocks
    private TournamentService testee;

    @Test
    public void updateTournament_always_scorerResetedAndGamesUpdated() {
        // arrange
        final TournamentDto tournamentDto = new TournamentDto();
        tournamentDto.setId("some-id");
        final List<GameDto> gameDtos = new ArrayList<>();
        final GameDto gameDto = new GameDto();
        gameDto.setScore1(1);
        gameDto.setScore2(2);
        gameDto.setId(1L);
        gameDtos.add(gameDto);
        tournamentDto.setGames(gameDtos);
        final Tournament tournament = new Tournament();
        tournament.setId("original-id");
        List<Game> games = new ArrayList<>();
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        games.add(game);
        tournament.setGames(games);
        when(tournamentRepository.findById(Mockito.anyString())).thenReturn(Optional.of(tournament));
        // act
        testee.updateTournament(tournamentDto);
        // assert
        assertEquals("original-id", tournament.getId());
        verify(scorerRepository).deleteAll(ArgumentMatchers.anyIterable());
        verify(game, never()).setId(Mockito.anyLong());
        verify(game).setScore1(1);
        verify(game).setScore2(2);
    }

    @Test
    public void updateTournament_gameNotFinished_scoreReset() {
        // arrange
        final TournamentDto tournamentDto = new TournamentDto();
        tournamentDto.setId("some-id");
        final List<GameDto> gameDtos = new ArrayList<>();
        final GameDto gameDto = new GameDto();
        gameDto.setScore1(null);
        gameDto.setScore2(null);
        gameDto.setId(1L);
        gameDtos.add(gameDto);
        tournamentDto.setGames(gameDtos);
        final Tournament tournament = new Tournament();
        tournament.setId("original-id");
        List<Game> games = new ArrayList<>();
        final Game game = mock(Game.class);
        when(game.getId()).thenReturn(1L);
        games.add(game);
        tournament.setGames(games);
        when(tournamentRepository.findById(Mockito.anyString())).thenReturn(Optional.of(tournament));
        // act
        testee.updateTournament(tournamentDto);
        // assert
        assertEquals("original-id", tournament.getId());
        verify(game).setScore1(-1);
        verify(game).setScore2(-1);
    }
}