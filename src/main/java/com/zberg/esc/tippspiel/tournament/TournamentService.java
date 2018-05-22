package com.zberg.esc.tippspiel.tournament;

import com.zberg.esc.tippspiel.tournament.db.PlayerRepository;
import com.zberg.esc.tippspiel.tournament.db.ScorerRepository;
import com.zberg.esc.tippspiel.tournament.db.TeamRepository;
import com.zberg.esc.tippspiel.tournament.db.TournamentRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.Game;
import com.zberg.esc.tippspiel.tournament.db.entities.Player;
import com.zberg.esc.tippspiel.tournament.db.entities.Scorer;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import com.zberg.esc.tippspiel.tournament.dto.PlayerDto;
import com.zberg.esc.tippspiel.tournament.dto.TournamentDto;
import com.zberg.esc.tippspiel.tournament.dto.mapper.PlayerMapper;
import com.zberg.esc.tippspiel.tournament.dto.mapper.TournamentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper = new PlayerMapper();
    private final TournamentMapper tournamentMapper = new TournamentMapper();
    private final ScorerRepository scorerRepository;
    private final PlayerService playerService;

    @Value("${tournament.id}")
    private String tournamentId;


    @Autowired
    public TournamentService(final TournamentRepository tournamentRepository, final PlayerRepository playerRepository, final TeamRepository teamRepository, final ScorerRepository scorerRepository, final PlayerService playerService) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
        this.teamRepository = teamRepository;
        this.scorerRepository = scorerRepository;
    }

    public Tournament get() {
        return getById(tournamentId);
    }

    public TournamentDto getActiveTournamnet() {
        return tournamentMapper.toDto(get());
    }

    private Tournament getById(final String id) {
        if (null == id) {
            throw new TournamentNotFoundException("id may not be null");
        }
        return tryLoadingTournament(id);
    }

    private Tournament tryLoadingTournament(String id) {
        return tournamentRepository.findById(id)//
                .orElseThrow(() -> new TournamentNotFoundException(String.format("Tournament '%s' not found", id)));
    }

    public PlayerDto loadPlayer(final Long playerId) {
        if (null == playerId) {
            throw new IllegalArgumentException("playerId may not be null");
        }
        final Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException("Player does not exist: " + playerId));
        return playerMapper.toDto(player);
    }

    public PlayerDto createNewPlayer() {
        return playerService.createNewPlayer(get());

    }

    @Transactional
    public void saveOrUpdatePlayer(final PlayerDto playerDto) {
        playerService.saveOrUpdatePlayer(playerDto, getById(playerDto.getTournamentId()));
    }

    @Transactional
    public void updateTournament(TournamentDto tournamentDto) {
        final Tournament tournament = getById(tournamentDto.getId());
        if (tournamentDto.getChampionId() != null && tournamentDto.getChampionId() >= 0) {
            tournament.setChampion(teamRepository.findById(tournamentDto.getChampionId()).orElse(null));
        } else {
            tournament.setChampion(null);
        }
        scorerRepository.deleteAll(tournament.getTopScorers());
        if (tournamentDto.getScorers() != null) {
            tournamentDto.getScorers().forEach(s -> {
                final Scorer scorer = new Scorer();
                scorer.setTournament(tournament);
                scorer.setName(s);
                scorerRepository.save(scorer);
            });
        }
        tournament.setScoreRange(tournamentDto.getScoreRange());
        final Map<Long, Game> gameMap = tournament.getGames().stream().collect(Collectors.toMap(Game::getId, Function.identity()));
        if (tournamentDto.getGames() != null) {
            tournamentDto.getGames().forEach(g -> {
                final Game game = gameMap.get(g.getId());
                if (g.isPlayed()) {
                    game.setScore1(g.getScore1());
                    game.setScore2(g.getScore2());
                } else {
                    game.setScore1(-1);
                    game.setScore2(-1);
                }
            });
        }
    }

    public List<PlayerDto> getAllPlayers() {
        return get().getPlayers().stream().map(playerMapper::toDto).collect(Collectors.toList());
    }


    public void delete(PlayerDto player) {
        playerService.delete(player);
    }
}
