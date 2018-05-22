package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.tournament.db.entities.Game;
import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> findAllByTournament(final Tournament tournament);
}
