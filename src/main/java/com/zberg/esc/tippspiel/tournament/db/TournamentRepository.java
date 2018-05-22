package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.tournament.db.entities.Tournament;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, String> {
    // nothing to do
}
