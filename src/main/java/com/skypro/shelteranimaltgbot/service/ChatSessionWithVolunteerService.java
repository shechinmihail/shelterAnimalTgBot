package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.enums.SessionEnum;
import com.skypro.shelteranimaltgbot.repository.ChatSessionWithVolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatSessionWithVolunteerService {


    private final ChatSessionWithVolunteerRepository chatSessionRepository;

    public ChatSessionWithVolunteerService(ChatSessionWithVolunteerRepository chatSessionRepository) {
        this.chatSessionRepository = chatSessionRepository;
    }


    /**
     * создание сессии
     */
    public ChatSessionWithVolunteer createSession(ChatSessionWithVolunteer session) {
        return chatSessionRepository.save(session);
    }


    /**
     * получить сессию по id, изменить статус
     */
    public ChatSessionWithVolunteer getChatSessionForReplaceStatus(Long idSession, SessionEnum session) {
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
     */
    public ChatSessionWithVolunteer getChatUser(Long idSessionForConnect) {
        return chatSessionRepository.findChatSessionWithVolunteerById(idSessionForConnect);
    }

    /**
     * получение сессии по userId
     */
    public ChatSessionWithVolunteer getSession(Long userId) {
        return chatSessionRepository.findChatSessionWithVolunteerByTelegramIdUser(userId);
    }

    /**
     * получение всех сессий
     */
    public List<ChatSessionWithVolunteer> getAllSession() {
        return chatSessionRepository.findAll();
    }

    /**
     * получить id последней сессии
     */
    public Long getLastId(Long id) {
        return chatSessionRepository.findId(id);
    }
}
