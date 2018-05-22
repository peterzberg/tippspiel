package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.security.UserRepository;
import com.zberg.esc.tippspiel.tournament.db.entities.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbTest {

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private BetRepository betRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ScorerRepository scorerRepository;
    @Autowired
    private TeamRepository teamRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Rule
    public TestName nameRule = new TestName();


    @Test
    @Transactional
    public void persistAndLoadEveryEntity() { // i was lazy, that's why there is one test for everything :)
        // arrange
        final Tournament testTournament = tournamentRepository.save(createTournament());
        Scorer tournamentTopScorer = new Scorer();
        tournamentTopScorer.setName("Thomas Müller");
        tournamentTopScorer.setTournament(testTournament);
        scorerRepository.save(tournamentTopScorer);
        final Game game = createGame(testTournament);
        final Team team1 = teamRepository.save(createTeam("Team 1", 50L));
        game.setTeam1(team1);
        final Team team2 = teamRepository.save(createTeam("Team 2", 51L));
        game.setTeam2(team2);
        gameRepository.save(game);
        final Player player = playerRepository.save(createPlayer(testTournament, team2));
        final Bet bet = new Bet();
        bet.setGame(game);
        bet.setPlayer(player);
        bet.setScore1(1);
        bet.setScore2(2);
        betRepository.save(bet);
        entityManager.flush();
        entityManager.clear();
        // act
        final Optional<Tournament> result = tournamentRepository.findById(nameRule.getMethodName());
        // assert
        assertTrue(result.isPresent());
        final Tournament tournament = result.get();
        final List<Player> persistedPlayers = tournament.getPlayers();
        assertEquals(1, persistedPlayers.size());
        final Player persistedPlayer = persistedPlayers.get(0);
        assertTrue(persistedPlayer.getId() > 0);
        final List<Bet> bets = persistedPlayer.getBets();
        assertEquals("no bet saved", 1, bets.size());
        final Bet persistedBet = bets.iterator().next();
        assertNotNull("no id generated", persistedBet.getId());
        final Game persistedBetGame = persistedBet.getGame();
        assertNotNull("game id not generated", persistedBetGame.getId());
        final Team persistedTeam1 = persistedBetGame.getTeam1();
        assertNotNull("team1 id not generated", persistedTeam1.getId());
        final Team persistedTeam2 = persistedBetGame.getTeam2();
        assertNotNull("team2 id not generated", persistedTeam2.getId());
        assertNotEquals(team1.getId(), team2.getId());
        final Set<Scorer> persistedScorers = tournament.getTopScorers();
        assertEquals(1, persistedScorers.size());
        assertEquals("Thomas Müller", persistedScorers.iterator().next().getName());
    }

    @Test
    @Ignore("easy way to create games. not useful in lifecycle ;-)")
    public void gameCreator() {

        final List<String> gamesList = new ArrayList<>();
        // a
        gamesList.add("Russland:Saudi-Arabien");
        gamesList.add("Ägypten:Uruguay");
        gamesList.add("Russland:Ägypten");
        gamesList.add("Uruguay:Saudi-Arabien");
        gamesList.add("Uruguay:Russland");
        gamesList.add("Saudi-Arabien:Ägypten");

        // b
        gamesList.add("Marokko:Iran");
        gamesList.add("Portugal:Spanien");
        gamesList.add("Portugal:Marokko");
        gamesList.add("Iran:Spanien");
        gamesList.add("Iran:Portugal");
        gamesList.add("Spanien:Marokko");

        // c
        gamesList.add("Frankreich:Australien");
        gamesList.add("Peru:Dänemark");
        gamesList.add("Dänemark:Australien");
        gamesList.add("Frankreich:Peru");
        gamesList.add("Dänemark:Frankreich");
        gamesList.add("Australien:Peru");

        // d
        gamesList.add("Argentinien:Island");
        gamesList.add("Kroatien:Nigeria");
        gamesList.add("Argentinien:Kroatien");
        gamesList.add("Nigeria:Island");
        gamesList.add("Nigeria:Argentinien");
        gamesList.add("Island:Kroatien");

        // e
        gamesList.add("Brasilien:Schweiz");
        gamesList.add("Costa Rica:Serbien");
        gamesList.add("Brasilien:Costa Rica");
        gamesList.add("Serbien:Schweiz");
        gamesList.add("Serbien:Brasilien");
        gamesList.add("Schweiz:Costa Rica");

        // f
        gamesList.add("Deutschland:Mexiko");
        gamesList.add("Schweden:Südkorea");
        gamesList.add("Südkorea:Mexiko");
        gamesList.add("Deutschland:Schweden");
        gamesList.add("Südkorea:Deutschland");
        gamesList.add("Mexiko:Schweden");

        // g
        gamesList.add("Belgien:Panama");
        gamesList.add("Tunesien:England");
        gamesList.add("Belgien:Tunesien");
        gamesList.add("England:Panama");
        gamesList.add("England:Belgien");
        gamesList.add("Panama:Tunesien");

        // h
        gamesList.add("Kolumbien:Japan");
        gamesList.add("Polen:Senegal");
        gamesList.add("Japan:Senegal");
        gamesList.add("Polen:Kolumbien");
        gamesList.add("Japan:Polen");
        gamesList.add("Senegal:Kolumbien");


        final Map<String, Long> teamNameToIdMap = StreamSupport.stream(teamRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Team::getName, Team::getId));

        final String sql = "insert into game (id, order_pos, fk_team1_id, fk_team2_id, score1, score2, fk_tournament_id) values (nextval('game_id_seq'), %d, %d, %d, -1, -1, 'esc-wc-2018-russia');";

        for (int i = 0; i < gamesList.size(); i++) {
            final String names[] = gamesList.get(i).split(":");
            final Long t1 = determineId(names[0].trim(), teamNameToIdMap);
            final Long t2 = determineId(names[1].trim(), teamNameToIdMap);
            System.out.println(String.format(sql, i, t1, t2));
        }

    }

    private Long determineId(String possibleTeamName, Map<String, Long> teamNameToIdMap) {
        final Long teamId = teamNameToIdMap.get(possibleTeamName);
        if (null == teamId){
            throw new IllegalArgumentException("Team '" + possibleTeamName + "' does not exist");
        }
        return teamId;
    }

    private Tournament createTournament() {
        Tournament testTournament = new Tournament();
        testTournament.setId(nameRule.getMethodName());
        testTournament.setScoreRange(ScoreRange.R_121_130);
        testTournament.setName(nameRule.getMethodName());
        return testTournament;
    }

    private Game createGame(Tournament testTournament) {
        final Game game = new Game();
        game.setOrder(1);
        game.setScore1(1);
        game.setScore2(2);
        game.setTournament(testTournament);
        return game;
    }

    private Player createPlayer(Tournament testTournament, Team champion) {
        Player player = new Player();
        player.setFirstname("firstname");
        player.setLastname("lastname");
        player.setZip("zip");
        player.setCity("city");
        player.setStreet("street");
        player.setTournament(testTournament);
        player.setGuessedTopScorer("Thomas Müller");
        player.setScoreRange(ScoreRange.R_141_150);
        player.setGuessedChampion(champion);
        player.setLastChangedBy(userRepository.findByUsername("test"));
        return player;
    }

    private Team createTeam(String name, Long id) {
        final Team team = new Team();
        team.setName(name);
        team.setId(id);
        return team;
    }

}
