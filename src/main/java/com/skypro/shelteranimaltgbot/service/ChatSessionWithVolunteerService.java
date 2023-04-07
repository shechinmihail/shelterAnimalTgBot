package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;
import com.skypro.shelteranimaltgbot.repository.ChatSessionWithVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatSessionWithVolunteerService {


    @Autowired
    ChatSessionWithVolunteerRepository chatSessionRepository;

    /**
     * создание сессии
     * */
    public void createSession(ChatSessionWithVolunteer session) {
        chatSessionRepository.save(session);
    }


    /**
     * получить сессию по id, изменить статус
     * */
    public void getChatSession(Long idSession, SessionEnum session) {
        ChatSessionWithVolunteer chatSessionWithVolunteer = chatSessionRepository.findChatSessionWithVolunteerById(idSession);
        chatSessionWithVolunteer.setSession(session);
        chatSessionRepository.save(chatSessionWithVolunteer);
    }



    /**
     * проверка активности сессии
     * */
    public boolean checkSession(Long idSession) {
        return chatSessionRepository.findChatSessionWithVolunteerById(idSession).getSession().equals(SessionEnum.OPEN);
    }

    /**
     * получить id пользователя в чате
     * */
    public ChatSessionWithVolunteer getChatUserId(Long idSessionForConnect) {
        return chatSessionRepository.findChatSessionWithVolunteerById(idSessionForConnect);
    }
}
