package com.zberg.esc.tippspiel.tournament.dto;

import com.zberg.esc.tippspiel.tournament.db.entities.ScoreRange;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PlayerDto {
    private Long id;
    @NotNull
    private String tournamentId;
    @NotNull(message = "Vorname ist Pflicht!")
    @Size(min = 2, message = "Vorname ist Pflicht!")
    private String firstname;
    @NotNull(message = "Nachname ist Pflicht!")
    @Size(min = 2, message = "Nachname ist Pflicht!")
    private String lastname;
    @NotNull(message = "Strasse ist Pflicht!")
    @Size(min = 2, message = "Strasse ist Pflicht!")
    private String street;
    @NotNull(message = "PLZ ist Pflicht!")
    @Size(min = 2, message = "PLZ ist Pflicht!")
    private String zip;
    @NotNull(message = "Ort ist Pflicht")
    @Size(min = 2, message = "Ort ist Pflicht!")
    private String city;
    @Valid
    private List<BetDto> bets;
    @NotNull(message = "Top-Scorer ist Pflicht")
    @Size(min = 2, message = "Top-Scorer ist Pflicht")
    private String guessedTopScorer; // ugly hack, but time is money ;-)
    @NotNull
    private ScoreRange scoreRange;
    @NotNull
    @Min(0)
    private Long guessedChampion;
}
