package com.zberg.esc.tippspiel.tournament;

import com.zberg.esc.tippspiel.security.UserRepository;
import com.zberg.esc.tippspiel.security.entities.User;
import com.zberg.esc.tippspiel.tournament.db.BetRepository;
import com.zberg.esc.tippspiel.tournament.db.GameRepository;
import com.zberg.esc.tippspiel.tournament.db.PlayerRepository;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.Bet;
import com.zberg.esc.tippspiel.tournament.db.entities.Game;
import com.zberg.esc.tippspiel.tournament.db.entities.Player;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import com.zberg.esc.tippspiel.tournament.dto.BetDto;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final BetRepository betRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    PlayerService(PlayerRepository playerRepository, GameRepository gameRepository, BetRepository betRepository, TeamRepository teamRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.betRepository = betRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    private void savePlayer(PlayerDto playerDto, final Tournament tournament) {
        final Player player = new Player();
        setPlayerData(player, playerDto);
        player.setTournament(tournament);
        final Player persistedPlayer = playerRepository.save(player);
        player.setBets(playerDto.getBets().stream().map(b -> {
            final Bet bet = new Bet();
            bet.setPlayer(persistedPlayer);
            bet.setScore1(b.getScore1());
            bet.setScore2(b.getScore2());
            bet.setGame(gameRepository.findById(b.getGameId()).orElseThrow(() -> new IllegalStateException("Game does not exist: " + b.getGameId())));
            betRepository.save(bet);
            return bet;
        }).collect(Collectors.toList()));
    }

    private void setPlayerData(Player player, PlayerDto playerDto) {
        player.setFirstname(playerDto.getFirstname());
        player.setLastname(playerDto.getLastname());
        player.setStreet(playerDto.getStreet());
        player.setZip(playerDto.getZip());
        player.setCity(playerDto.getCity());
        player.setGuessedTopScorer(playerDto.getGuessedTopScorer());
        player.setScoreRange(playerDto.getScoreRange());
        player.setGuessedChampion(teamRepository.findById(playerDto.getGuessedChampion()).orElseThrow(() -> new IllegalStateException("Team does not exist: " + playerDto.getGuessedChampion())));
        player.setLastChangedBy(getLoggedInUser());

    }

    // visible for testing
    User getLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            final String username = authentication.getName();
            return userRepository.findByUsername(username);
        }
        throw new IllegalStateException("there has to be a logged-in user");
    }

    private void updatePlayer(PlayerDto playerDto) {
        final Player player = playerRepository.findById(playerDto.getId()).orElseThrow(() -> new PlayerNotFoundException("Player does not exist: " + playerDto.getId()));
        setPlayerData(player, playerDto);
        final Map<Long, Bet> betMap = player.getBets().stream().collect(Collectors.toMap(Bet::getId, Function.identity()));
        playerDto.getBets().forEach(dto -> {
            final Bet bet = betMap.get(dto.getId());
            bet.setScore1(dto.getScore1());
            bet.setScore2(dto.getScore2());
        });
    }

    void saveOrUpdatePlayer(PlayerDto playerDto, final Tournament tournament) {
        if (playerDto.getId() != null) {
            updatePlayer(playerDto);
        } else {
            savePlayer(playerDto, tournament);
        }
    }

    PlayerDto createNewPlayer(final Tournament tournament) {
        final PlayerDto player = new PlayerDto();
        player.setTournamentId(tournament.getId());
        final List<Game> games = tournament.getGames();
        player.setBets(
                games.stream()
                        .sorted(Comparator.comparingInt(Game::getOrder))
                        .map(g -> {
                            final BetDto bet = new BetDto();
                            bet.setGameId(g.getId());
                            bet.setTeam1(g.getTeam1().getName());
                            bet.setTeam2(g.getTeam2().getName());
                            return bet;
                        }).collect(Collectors.toList()));
        return player;
    }

    void delete(PlayerDto playerDto) {
        playerRepository.findById(playerDto.getId()).ifPresent(p -> {
            betRepository.deleteAll(p.getBets());
            playerRepository.delete(p);
        });
    }
}
