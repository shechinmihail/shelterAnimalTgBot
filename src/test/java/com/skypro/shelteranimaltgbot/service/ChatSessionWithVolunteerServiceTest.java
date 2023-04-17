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

import static org.mockito.Mockito.lenient;

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
    void getChatSessionForClose() {
        ChatSessionWithVolunteer chatSession = new ChatSessionWithVolunteer(1L, 1L, SessionEnum.STANDBY);
        chatSession.setSession(SessionEnum.OPEN);
        chatSessionWithVolunteer.setSession(SessionEnum.OPEN);
        Assertions.assertEquals(chatSession, chatSessionWithVolunteer);

    }

    @Test
    void checkSession() {
        ChatSessionWithVolunteer chatSession = new ChatSessionWithVolunteer(1L, 1L, SessionEnum.OPEN);
        Assertions.assertEquals(chatSession.getSession(), SessionEnum.OPEN);

    }

    @Test
    void getChatUser() {
        ChatSessionWithVolunteer chatSession = new ChatSessionWithVolunteer(1L, 1L, SessionEnum.OPEN);
        Assertions.assertEquals(chatSessionWithVolunteerService.getChatUser(1L), chatSession.getId());
    }
}