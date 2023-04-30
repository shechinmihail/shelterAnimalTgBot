package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;
import com.skypro.shelteranimaltgbot.repository.ChatSessionWithVolunteerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatSessionWithVolunteerServiceTest {

    @Mock
    ChatSessionWithVolunteerRepository chatSessionRepository;

    @InjectMocks
    ChatSessionWithVolunteerService chatSessionWithVolunteerService;


    ChatSessionWithVolunteer chatSessionWithVolunteer;

    @BeforeEach
    void setUp() {
        chatSessionWithVolunteer = new ChatSessionWithVolunteer(1L, 1L, SessionEnum.STANDBY);
        lenient().when(chatSessionWithVolunteerService.createSession(chatSessionWithVolunteer))
                .thenReturn(chatSessionWithVolunteer);


    }

    @Test
    void createSession() {
        ChatSessionWithVolunteer chatSession = new ChatSessionWithVolunteer(1L, 1L, SessionEnum.STANDBY);
        Assertions.assertEquals(chatSession, chatSessionWithVolunteer);
    }

    @Test
    void getChatSessionForReplaceStatus() {
        chatSessionWithVolunteer.setSession(SessionEnum.OPEN);
        when(chatSessionRepository.findChatSessionWithVolunteerById(3L)).thenReturn(chatSessionWithVolunteer);
        Assertions.assertEquals(chatSessionWithVolunteer, chatSessionWithVolunteerService.getChatSessionForReplaceStatus(3L, SessionEnum.OPEN));

    }

    @Test
    void checkSession() {
        chatSessionWithVolunteer.setSession(SessionEnum.OPEN);
        when(chatSessionRepository.findChatSessionWithVolunteerById(3L)).thenReturn(chatSessionWithVolunteer);
        Assertions.assertTrue(chatSessionWithVolunteerService.checkSession(3L));
    }

    @Test
    void getSession() {
        when(chatSessionRepository.findChatSessionWithVolunteerByTelegramIdUser(3L)).thenReturn(chatSessionWithVolunteer);
        Assertions.assertEquals(chatSessionWithVolunteer, chatSessionWithVolunteerService.getSession(3L));
    }

    @Test
    void getChatUser() {
        ChatSessionWithVolunteer chatSession = new ChatSessionWithVolunteer(1L, 1L, SessionEnum.OPEN);
        Assertions.assertEquals(chatSessionWithVolunteerService.getChatUser(1L), chatSession.getId());
    }

    @Test
    void getAllSession() {
        List<ChatSessionWithVolunteer> chatSessionWithVolunteers = new ArrayList<>();
        chatSessionWithVolunteers.add(chatSessionWithVolunteer);
        when(chatSessionRepository.findAll()).thenReturn(chatSessionWithVolunteers);
        Assertions.assertEquals(chatSessionWithVolunteerService.getAllSession(), chatSessionWithVolunteers);

    }

    @Test
    void getLastId() {
        Long id = 3L;
        when(chatSessionRepository.findId(id)).thenReturn(id);
        Assertions.assertEquals(chatSessionWithVolunteerService.getLastId(3L), id);
    }
}