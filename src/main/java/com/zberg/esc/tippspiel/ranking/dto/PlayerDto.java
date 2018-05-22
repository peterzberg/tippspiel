package com.zberg.esc.tippspiel.ranking.dto;

import lombok.Data;

@Data
public class PlayerDto {
    private String firstname;
    private String lastname;
    private String street;
    private String zip;
    private String city;
    private long id;
}
