package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Document;
import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TypePetServiceTest {

    @Mock
    TypePetRepository typePetRepository;

    @InjectMocks
    TypePetService typePetService;

    List<TypePet> typePets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        TypePet typePet = new TypePet("Собака", new Document("Passport"));
        TypePet typePet1 = new TypePet("Кошка", new Document("Passport"));
        typePets = List.of(typePet, typePet1);
        when(typePetRepository.findAll()).thenReturn(typePets);
    }

    @Test
    void getAllTypePet() {
        TypePet tP = new TypePet("Собака", new Document("Passport"));
        TypePet tP1 = new TypePet("Кошка", new Document("Passport"));
        Set<TypePet> typePetsList = new HashSet<>(List.of(tP, tP1));
        Assertions.assertEquals(typePetsList, typePetService.getAllTypePet());
    }
}