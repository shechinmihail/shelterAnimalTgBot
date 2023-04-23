package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс ChatSessionWithVolunteerRepository
 * для работы с БД (для чата волонтера)
 */
@Repository
public interface ChatSessionWithVolunteerRepository extends JpaRepository<ChatSessionWithVolunteer, Long> {
    /**
     * возврат сессии по id
     */
    ChatSessionWithVolunteer findChatSessionWithVolunteerById(Long chatId);


    ChatSessionWithVolunteer findChatSessionWithVolunteerByTelegramIdUser(Long userChatId);

    @Query("select max(c.id) from ChatSessionWithVolunteer c where c.telegramIdUser = :id or c.telegramIdVolunteer =:id")
    Long findId(@Param("id") Long id);
}
