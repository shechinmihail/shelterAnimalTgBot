package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Shelter;
import com.skypro.shelteranimaltgbot.repository.ShelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShelterService {

    @Autowired
    ShelterRepository shelterRepository;


    public String getAbout(String userNameShelter) {
        Shelter shelter = new Shelter(shelterRepository.findByUserName(userNameShelter));
        if (shelter.getAbout() != null) {
            return shelter.getAbout();
        }
        return "Описание нет";
    }
}
