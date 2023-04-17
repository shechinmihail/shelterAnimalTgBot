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
     */
    public ChatSessionWithVolunteer createSession(ChatSessionWithVolunteer session) {
        return chatSessionRepository.save(session);
    }


    /**
     * получить сессию по id, изменить статус
     */
    public ChatSessionWithVolunteer getChatSessionForClose(Long idSession, SessionEnum session) {
        ChatSessionWithVolunteer chatSessionWithVolunteer = chatSessionRepository.findChatSessionWithVolunteerById(idSession);
        chatSessionWithVolunteer.setSession(session);
        return chatSessionRepository.save(chatSessionWithVolunteer);
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
    public ChatSessionWithVolunteer getChatUser(Long idSessionForConnect) {
        return chatSessionRepository.findChatSessionWithVolunteerById(idSessionForConnect);
    }
}
