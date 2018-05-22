package com.zberg.esc.tippspiel.security.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tippspiel_user")
public class User {
    @Id
    @SequenceGenerator(name = "user_id_seq",
            sequenceName = "user_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_id_seq")
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "user_name")
    private String username;

    private String password;

    private boolean enabled;
}
