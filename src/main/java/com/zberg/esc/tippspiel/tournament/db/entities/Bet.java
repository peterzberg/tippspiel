package com.zberg.esc.tippspiel.tournament.db.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Bet {
    @Id
    @SequenceGenerator(name = "bet_id_seq",
            sequenceName = "bet_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "bet_id_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fk_game_id")
    private Game game;
    @ManyToOne
    @JoinColumn(name = "fk_player_id")
    private Player player;
    private int score1;
    private int score2;
}
