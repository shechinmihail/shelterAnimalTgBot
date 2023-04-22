package com.skypro.shelteranimaltgbot.repository;


import com.skypro.shelteranimaltgbot.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс ShelterRepository
 * для работы с БД (для приюта)
 */
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Shelter findByUserName(@Param("userNameShelter") String usernameShelter);
}