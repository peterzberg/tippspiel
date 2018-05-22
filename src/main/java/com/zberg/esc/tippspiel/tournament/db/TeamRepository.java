package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.tournament.db.entities.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
