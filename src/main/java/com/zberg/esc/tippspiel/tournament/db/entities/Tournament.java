package com.zberg.esc.tippspiel.tournament.db.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Tournament {
    @Id
    private String id;
    private String name;
    @OneToMany(mappedBy = "tournament")
    @OrderBy("order asc")
    private List<Game> games = new ArrayList<>();
    @OneToMany(mappedBy = "tournament")
    private List<Player> players = new ArrayList<>();
    @OneToMany(mappedBy = "tournament")
    private Set<Scorer> topScorers = new HashSet<>(); // ugly hack, but time is money ;-)
    @Column(name = "score_range")
    private ScoreRange scoreRange;
    @OneToOne
    @JoinColumn(name = "fk_champion_id")
    private Team champion;
}
