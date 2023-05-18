package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.model.Enum.ProbationPeriod;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    Adoption findAdoptionById(Long id);

    Adoption findAdoptionByPetAndUserAndProbationPeriod(Pet pet, User user, ProbationPeriod passing);
}