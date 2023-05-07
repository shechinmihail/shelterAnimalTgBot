package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.model.Enum.ProbationPeriod;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    boolean findAdoptionById(Long id);

    Collection<Adoption> findAllByPet(Pet pet);

    Adoption findAdoptionByPetAndUserAndProbationPeriod(Pet pet, User user, ProbationPeriod passing);
}