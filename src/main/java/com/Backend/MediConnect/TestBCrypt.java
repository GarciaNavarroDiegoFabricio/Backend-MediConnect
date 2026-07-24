package com.Backend.MediConnect;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "medico123";

        String hash = encoder.encode(password);

        System.out.println("Password original: " + password);
        System.out.println("Hash BCrypt: " + hash);
    }
}