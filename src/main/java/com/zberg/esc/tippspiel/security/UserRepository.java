package com.zberg.esc.tippspiel.security;

import com.zberg.esc.tippspiel.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
