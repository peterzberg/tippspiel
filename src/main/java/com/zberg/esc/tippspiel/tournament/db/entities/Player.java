package com.zberg.esc.tippspiel.tournament.db.entities;

import com.zberg.esc.tippspiel.security.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
public class Player {
    @Id
    @SequenceGenerator(name = "player_id_seq",
            sequenceName = "player_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "player_id_seq")
    private Long id;
    @NotNull
    @Size(min = 1, max = 200)
    private String firstname;
    @NotNull
    @Size(min = 1, max = 200)
    private String lastname;
    @NotNull
    @Size(min = 1, max = 200)
    private String street;
    @NotNull
    @Size(min = 1, max = 10)
    private String zip;
    @NotNull
    @Size(min = 1, max = 200)
    private String city;
    @OneToMany(mappedBy = "player")
    private List<Bet> bets;
    @ManyToOne
    @JoinColumn(name = "fk_tournament_id", nullable = false)
    @NotNull
    private Tournament tournament;
    @Column(name = "guessed_top_scorer")
    @NotNull
    private String guessedTopScorer; // ugly hack, but time is money ;-)
    @Column(name = "score_range")
    @NotNull
    private ScoreRange scoreRange;
    @OneToOne
    @JoinColumn(name = "fk_champion_id")
    @NotNull
    private Team guessedChampion;
    @OneToMany
    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User lastChangedBy;

}
