package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.tournament.db.entities.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    // nothing to to
}
