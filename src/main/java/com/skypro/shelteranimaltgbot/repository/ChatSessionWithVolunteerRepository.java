package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionWithVolunteerRepository extends JpaRepository<ChatSessionWithVolunteer, Long> {
    /**
     * возврат сессии по id
     *
     * */
    ChatSessionWithVolunteer findChatSessionWithVolunteerById(Long idChat);



}
