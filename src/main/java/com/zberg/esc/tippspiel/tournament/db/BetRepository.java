package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.tournament.db.entities.Bet;
import org.springframework.data.repository.CrudRepository;

public interface BetRepository extends CrudRepository<Bet, Long> {
}
