package com.skypro.shelteranimaltgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShelterAnimalTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShelterAnimalTgBotApplication.class, args);
    }

}
