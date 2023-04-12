package com.skypro.shelteranimaltgbot.service;


import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TypePetService {

    private final TypePetRepository typePetRepository;

    public TypePetService(TypePetRepository typePetRepository) {
        this.typePetRepository = typePetRepository;
    }

    public Collection<TypePet> getAllTypePet() {
        return typePetRepository.findAll();
    }

}
