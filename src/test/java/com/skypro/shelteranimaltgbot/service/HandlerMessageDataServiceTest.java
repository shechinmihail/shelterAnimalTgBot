package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandlerMessageDataServiceTest {

    @Mock
    private CommandButtonService commandButtonService;
    @Mock
    private TelegramBot telegramBot;

    @Mock
    private SendReportService sendReportService;
    @Mock
    private UserService userService;

    @Mock
    private ChatSessionWithVolunteerService chatSessionService;

    private ArrayList<SendMessage> messages = new ArrayList<>();
    private final String START = "/start";
    private final String CALL_VOLUNTEER = "Позвать волонтера";
    private final String OPEN = "Принять";
    private final String CLOSE = "Закрыть/Отклонить";

    @InjectMocks
    private HandlerMessageDataService handlerMessageDataService;

    @Test
    public void getStartMessage() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        when(message.from()).thenReturn(user);
        when(update.message()).thenReturn(message);
        when(message.from().id()).thenReturn(123L);
        when(update.message().text()).thenReturn(START);

        handlerMessageDataService.handlerMessageData(update, messages);

        verify(commandButtonService).mainMenu(update, messages);
        verify(commandButtonService, never()).callVolunteer(update, messages);
        verify(commandButtonService, never()).openСonnection(messages);
        verify(commandButtonService, never()).sendNotification(update, messages);
        verify(sendReportService, never()).saveReport(update);
        verify(telegramBot, never()).execute(any(ForwardMessage.class));
    }

    @Test
    void testWithCallVolunteerCommand() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        when(message.from()).thenReturn(user);
        when(update.message()).thenReturn(message);
        when(message.from().id()).thenReturn(123L);
        when(update.message().text()).thenReturn(CALL_VOLUNTEER);

        handlerMessageDataService.handlerMessageData(update, messages);

        verify(commandButtonService, never()).mainMenu(update, messages);
        verify(commandButtonService).callVolunteer(update, messages);
        verify(commandButtonService, never()).openСonnection(messages);
        verify(commandButtonService, never()).closeСonnection(update, messages);
        verify(commandButtonService, never()).sendNotification(update, messages);
        verify(sendReportService, never()).saveReport(update);
        verify(telegramBot, never()).execute(any(ForwardMessage.class));
    }

    @Test
    void testWithOpenConnection() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        when(message.from()).thenReturn(user);
        when(update.message()).thenReturn(message);
        when(message.from().id()).thenReturn(123L);
        when(update.message().text()).thenReturn(OPEN);

        handlerMessageDataService.handlerMessageData(update, messages);

        verify(commandButtonService, never()).mainMenu(update, messages);
        verify(commandButtonService, never()).callVolunteer(update, messages);
        verify(commandButtonService).openСonnection(messages);
        verify(commandButtonService, never()).closeСonnection(update, messages);
        verify(commandButtonService, never()).sendNotification(update, messages);
        verify(sendReportService, never()).saveReport(update);
        verify(telegramBot, never()).execute(any(ForwardMessage.class));
    }

    @Test
    void testWithCloseConnection() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        when(message.from()).thenReturn(user);
        when(update.message()).thenReturn(message);
        when(message.from().id()).thenReturn(123L);
        when(update.message().text()).thenReturn(CLOSE);

        handlerMessageDataService.handlerMessageData(update, messages);

        verify(commandButtonService, never()).mainMenu(update, messages);
        verify(commandButtonService, never()).callVolunteer(update, messages);
        verify(commandButtonService, never()).openСonnection(messages);
        verify(commandButtonService).closeСonnection(update, messages);
        verify(commandButtonService, never()).sendNotification(update, messages);
        verify(sendReportService, never()).saveReport(update);
        verify(telegramBot, never()).execute(any(ForwardMessage.class));
    }

    @Test
    void testGetPhoto() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        PhotoSize photoSize = mock(PhotoSize.class);


        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(123L);
        when(update.message().text()).thenReturn(null);
        when(update.message().photo()).thenReturn(new PhotoSize[]{photoSize});
        when(update.message().caption()).thenReturn("Caption");
        when(userService.checkUserStatus(123L)).thenReturn(StatusEnum.ADOPTER);

        handlerMessageDataService.handlerMessageData(update, messages);

        verify(sendReportService).saveReport(update);
        verify(commandButtonService, never()).mainMenu(update, messages);
        verify(commandButtonService, never()).callVolunteer(update, messages);
        verify(commandButtonService, never()).openСonnection(messages);
        verify(commandButtonService, never()).closeСonnection(update, messages);
        verify(commandButtonService, never()).sendNotification(update, messages);
        verify(telegramBot, never()).execute(any(ForwardMessage.class));
    }

    @Test
    void testGetContact() {
        // Создание макетов объектов
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        Contact contact = mock(Contact.class);

        // Настройка поведения макетов объектов
        when(update.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(123L);
        when(update.message().text()).thenReturn(null);
        when(update.message().contact()).thenReturn(contact);
        when(message.from().firstName()).thenReturn("Иван");

        // Вызов тестируемого метода
        handlerMessageDataService.handlerMessageData(update, messages);

        // Проверка вызовов методов
        verify(commandButtonService).setContact(update);
        verify(commandButtonService).sendNotification(update, messages);
        verify(sendReportService, never()).saveReport(update);
        verify(commandButtonService, never()).mainMenu(update, messages);
        verify(commandButtonService, never()).callVolunteer(update, messages);
        verify(commandButtonService, never()).openСonnection(messages);
        verify(commandButtonService, never()).closeСonnection(update, messages);
        verify(telegramBot, never()).execute(any(ForwardMessage.class));
    }

    @Test
    public void testChatWithVolunteer() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        ChatSessionWithVolunteer chatSessionWithVolunteer = mock(ChatSessionWithVolunteer.class);
        SendResponse sendResponse = mock(SendResponse.class);
        when(message.from()).thenReturn(user);
        when(update.message()).thenReturn(message);
        when(message.from().id()).thenReturn(123L);
        when(update.message().text()).thenReturn("Some text");
        when(chatSessionService.getLastId(123L)).thenReturn(1L);
        when(chatSessionService.getChatUser(1L)).thenReturn(chatSessionWithVolunteer);
        when(chatSessionService.checkSession(1L)).thenReturn(true);
        when(telegramBot.execute(any(ForwardMessage.class))).thenReturn(sendResponse);

        handlerMessageDataService.handlerMessageData(update, messages);

        verify(telegramBot).execute(any(ForwardMessage.class));
        verify(commandButtonService, never()).mainMenu(update, messages);
        verify(commandButtonService, never()).callVolunteer(update, messages);
        verify(commandButtonService, never()).openСonnection(messages);
        verify(commandButtonService, never()).sendNotification(update, messages);
        verify(sendReportService, never()).saveReport(update);
    }
}
