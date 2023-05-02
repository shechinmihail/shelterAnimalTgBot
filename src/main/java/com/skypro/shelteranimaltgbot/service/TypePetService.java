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

    /**
     * метод возвращает все типы животных
     */
    public Set<TypePet> getAllTypePet() {
        Set<TypePet> typePets = new HashSet<>(typePetRepository.findAll());
        return typePets;
    }

    /**
     * метод возвращает все типы животных по странично
     */
    public List<TypePet> findAllByPagination(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return typePetRepository.findAll(pageRequest).getContent();
    }


    /**
     * метод возвращает тип животного по наименованию типа
     */
    public TypePet findPetByType(String text) {
        return typePetRepository.findTypePetByTypeOrderById(text);
    }
}
