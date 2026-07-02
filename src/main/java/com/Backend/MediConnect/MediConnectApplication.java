package com.Backend.MediConnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; //  IMPORTANTE

@SpringBootApplication
@EnableScheduling //  ESTO ACTIVA LOS GATILLOS AUTOMÁTICOS
public class MediConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediConnectApplication.class, args);
    }
}