package com.zberg.esc.tippspiel.tournament;

import com.zberg.esc.tippspiel.security.UserRepository;
import com.zberg.esc.tippspiel.security.entities.User;
import com.zberg.esc.tippspiel.tournament.db.BetRepository;
import com.zberg.esc.tippspiel.tournament.db.GameRepository;
import com.zberg.esc.tippspiel.tournament.db.PlayerRepository;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.*;
import com.zberg.esc.tippspiel.tournament.dto.BetDto;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private BetRepository betRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;
    private PlayerService testee;

    @Before
    public void initTestee() {
        testee = new PlayerService(playerRepository, gameRepository, betRepository, teamRepository, userRepository) {
            @Override
            User getLoggedInUser() {
                final User user = new User();
                user.setId(22L);
                user.setUsername("test");
                return user;
            }
        };
    }

    @Test
    public void createNewPlayer_gamesExist_betForEveryGameCreatedOrderingCorrect() {
        // arrange
        final Tournament tournament = new Tournament();
        final List<Game> games = new ArrayList<>();
        games.add(createGame(1L, 2));
        games.add(createGame(2L, 1));
        tournament.setGames(games);
        // act
        final PlayerDto result = testee.createNewPlayer(tournament);
        // assert
        assertNotNull(result);
        final List<BetDto> bets = result.getBets();
        assertNotNull(bets);
        assertEquals(2, bets.size());
        assertEquals(2L, bets.get(0).getGameId().longValue());
        assertEquals(1L, bets.get(1).getGameId().longValue());

    }

    @Test
    public void updatePlayer_always_noIdsOverwritten() {
        // arrange
        final PlayerDto playerDto = new PlayerDto();
        playerDto.setId(1L);
        playerDto.setGuessedChampion(1L);
        playerDto.setBets(new ArrayList<>());
        final List<BetDto> betsDto = new ArrayList<>();
        final BetDto betDto = new BetDto();
        betDto.setGameId(40L);
        betDto.setId(1L);
        betDto.setScore1(1);
        betDto.setScore2(2);
        betsDto.add(betDto);
        playerDto.setBets(betsDto);
        final Player player = new Player();
        player.setId(2L);
        final List<Bet> bets = new ArrayList<>();
        final Bet bet = Mockito.mock(Bet.class);
        when(bet.getId()).thenReturn(1L);
        bets.add(bet);
        player.setBets(bets);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(new Team()));
        // act
        testee.saveOrUpdatePlayer(playerDto, null);
        // assert
        assertEquals("id overwritten!", 2L, player.getId().longValue());
        verify(bet, never()).setId(Mockito.anyLong());
        assertEquals(2L, player.getId().longValue());
    }

    private Game createGame(Long gameId, int order) {
        final Game result = new Game();
        result.setId(gameId);
        result.setOrder(order);
        result.setTeam1(createTeam(1L, "Team" + gameId + "_1"));
        result.setTeam2(createTeam(2L, "Team" + gameId + "_2"));
        return result;
    }

    private Team createTeam(long teamId, String name) {
        final Team result = new Team();
        result.setId(teamId);
        result.setName(name);
        return result;
    }

}