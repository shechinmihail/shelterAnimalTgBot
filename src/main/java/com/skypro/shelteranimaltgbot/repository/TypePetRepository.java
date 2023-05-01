package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.TypePet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePetRepository extends JpaRepository<TypePet, Long> {

    TypePet findTypePetByType(String text);
}

