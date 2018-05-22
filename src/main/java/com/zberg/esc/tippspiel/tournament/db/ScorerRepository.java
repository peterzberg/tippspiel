package com.zberg.esc.tippspiel.tournament.db;

import com.zberg.esc.tippspiel.tournament.db.entities.Scorer;
import org.springframework.data.repository.CrudRepository;

public interface ScorerRepository extends CrudRepository<Scorer, Long> {
}
