package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс ReportRepository
 * Используется для работы с БД (для отчетов по домашним питомцам)
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
