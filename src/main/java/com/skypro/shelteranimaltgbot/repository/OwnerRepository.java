package com.skypro.shelteranimaltgbot.repository;


import com.skypro.shelteranimaltgbot.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс OwnerRepository
 * Для работы с БД (для владельца домашнего питомца)
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

}
