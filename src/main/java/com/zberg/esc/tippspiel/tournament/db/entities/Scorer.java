package com.zberg.esc.tippspiel.tournament.db.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Scorer {
    @Id
    @SequenceGenerator(name = "scorer_id_seq",
            sequenceName = "scorer_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "scorer_id_seq")
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "fk_tournament_id")
    private Tournament tournament;
}
