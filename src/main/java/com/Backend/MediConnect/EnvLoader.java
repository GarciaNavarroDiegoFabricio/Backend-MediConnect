package com.Backend.MediConnect;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

    public static void load() {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        System.setProperty("JPA_DEFAULT_SCHEMA", dotenv.get("JPA_DEFAULT_SCHEMA"));
    }
}