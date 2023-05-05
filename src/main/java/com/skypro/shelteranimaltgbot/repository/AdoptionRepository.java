package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    boolean findAdoptionById(Long id);

}