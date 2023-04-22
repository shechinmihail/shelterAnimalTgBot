package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс ChatSessionWithVolunteerRepository
 * для работы с БД (для чата волонтера)
 */
@Repository
public interface ChatSessionWithVolunteerRepository extends JpaRepository<ChatSessionWithVolunteer, Long> {
    /**
     * возврат сессии по id
     *
     * */
    ChatSessionWithVolunteer findChatSessionWithVolunteerById(Long chatId);


    ChatSessionWithVolunteer findChatSessionWithVolunteerByTelegramIdUser(Long userChatId);


}
