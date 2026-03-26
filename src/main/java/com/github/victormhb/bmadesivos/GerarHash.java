package com.github.victormhb.bmadesivos;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String senha = "admin";

        String hash = encoder.encode(senha);

        System.out.println(hash);
    }
}
