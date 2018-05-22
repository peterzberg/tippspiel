package com.zberg.esc.tippspiel;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        if (args.length == 0){
            System.err.println("no password to encode");
        } else {
            final String rawPw = args[0];
            final String encoded = new BCryptPasswordEncoder().encode(rawPw);
            System.out.println(encoded);
        }
    }
}
