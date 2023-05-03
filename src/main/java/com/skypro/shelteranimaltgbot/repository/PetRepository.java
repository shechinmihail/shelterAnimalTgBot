package com.skypro.shelteranimaltgbot.repository;


import com.skypro.shelteranimaltgbot.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс PetRepository
 * для работы с БД (для домашнего питомца)
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Pet findPetById(Long petId);
}
