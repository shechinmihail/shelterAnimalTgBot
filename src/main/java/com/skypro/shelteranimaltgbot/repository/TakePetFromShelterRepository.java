package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.TakePetFromShelter;
import com.skypro.shelteranimaltgbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс TakePetFromShelterRepository
 * Используется для работы с БД (забрать питомца из приюта)
 */
@Repository
public interface TakePetFromShelterRepository extends JpaRepository<TakePetFromShelter, Long> {
}
