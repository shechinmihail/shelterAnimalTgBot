package com.skypro.shelteranimaltgbot.listener;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.service.ForwardListenerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;


import static org.mockito.Mockito.*;

@SpringBootTest
class TelegramBotUpdatesListenerTest {

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @MockBean
    TelegramBot telegramBot;
    @MockBean
    ForwardListenerService forwardListenerService;

    @Test
    public void processTest() {
        Update updates = mock(Update.class);
        Message message = mock(Message.class);
        TelegramBot telegramBot = mock(TelegramBot.class);
        when(message.text()).thenReturn("/Test");
        when(updates.message()).thenReturn(message);
        ArgumentCaptor<SendMessage> messageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        when(telegramBot.execute(messageArgumentCaptor.capture())).thenReturn(null);
        telegramBotUpdatesListener.process(List.of(updates));

    }

}