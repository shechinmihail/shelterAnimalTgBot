package com.skypro.shelteranimaltgbot.service;


import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TypePetService {

    private final TypePetRepository typePetRepository;

    public TypePetService(TypePetRepository typePetRepository) {
        this.typePetRepository = typePetRepository;
    }

    public Set<TypePet> getAllTypePet() {
        Set<TypePet> typePets = new HashSet<>(typePetRepository.findAll());
        return typePets;
    }


    public List<TypePet> findAllByPagination(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return typePetRepository.findAll(pageRequest).getContent();
    }

    public TypePet findPetByType(String text) {
        return typePetRepository.findTypePetByType(text);
    }
}
