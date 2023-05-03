package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.TypePet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс TypePetRepository
 * для работы с БД (для вида домашнего питомца)
 */
@Repository
public interface TypePetRepository extends JpaRepository<TypePet, Long> {

    TypePet findTypePetByTypeOrderById(String text);
}

