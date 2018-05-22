package com.zberg.esc.tippspiel.tournament.db.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Game {
    @Id
    @SequenceGenerator(name = "game_id_seq",
            sequenceName = "game_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "game_id_seq")
    private Long id;
    @Column(name = "order_pos")
    private int order;
    @ManyToOne
    @JoinColumn(name = "fk_team1_id")
    private Team team1;
    @ManyToOne
    @JoinColumn(name = "fk_team2_id")
    private Team team2;
    private int score1 = -1;
    private int score2 = -1;
    @ManyToOne
    @JoinColumn(name = "fk_tournament_id")
    private Tournament tournament;

    public boolean isPlayed() {
        return score1 != -1 && score2 != -1;
    }
}
