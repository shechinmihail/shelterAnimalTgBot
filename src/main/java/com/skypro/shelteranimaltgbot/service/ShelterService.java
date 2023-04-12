package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShelterService {

    @Autowired
    ShelterRepository shelterRepository;


    public String getAbout(String userNameShelter) {
        return shelterRepository.findAboutByUserName(userNameShelter);
    }
}
