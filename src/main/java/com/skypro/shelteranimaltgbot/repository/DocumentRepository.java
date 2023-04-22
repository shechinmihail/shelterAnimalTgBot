package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс DocumentRepository
 * для работы с БД (для документов)
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
