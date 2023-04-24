package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForwardListenerServiceTest {

    @InjectMocks
    private ForwardListenerService forwardListenerService;

    @Mock
    private HandlerСalBakDataService handlerСalBakDataService;

    @Mock
    private HandlerMessageDataService handlerMessageDataService;

    @Test
    public void testMessagesWhenCallBackDataIsNotNullAndMessageIsNull() {
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(update.message()).thenReturn(null);

        forwardListenerService.messages(update);
        verify(handlerСalBakDataService, times(1)).handlerСalBakData(any(), any());
        verify(handlerMessageDataService, times(0)).handlerMessageData(any(), any());
    }

    @Test
    public void testMessagesWhenCallBackDataIsNullAndMessageIsNotNull() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.callbackQuery()).thenReturn(null);
        when(update.message()).thenReturn(message);

        forwardListenerService.messages(update);
        verify(handlerСalBakDataService, times(0)).handlerСalBakData(any(), any());
        verify(handlerMessageDataService, times(1)).handlerMessageData(any(), any());
    }

    @Test
    public void testMessagesWhenBothCallBackDataAndMessageAreNull() {
        Update update = mock(Update.class);
        when(update.callbackQuery()).thenReturn(null);
        when(update.message()).thenReturn(null);

        forwardListenerService.messages(update);
        verify(handlerMessageDataService, times(0)).handlerMessageData(any(), any());
        verify(handlerСalBakDataService, times(0)).handlerСalBakData(any(), any());

    }
}
