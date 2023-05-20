package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.TypePet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HandlerСalBakDataServiceTest {
    @Mock
    private ButtonService buttonService;

    @Mock
    private TypePetService typePetService;

    @Mock
    private PetService petService;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private ShelterService shelterService;

    @Mock
    private CommandButtonService commandButtonService;

    @Mock
    private TakePetFromShelterService takePetFromShelterService;

    @Mock
    private ReportService reportService;
    @Mock
    private SendReportService sendReportService;
    @Mock
    private UserService userService;

    @InjectMocks
    private HandlerСalBakDataService handlerСalBakDataService;

    private List<SendMessage> messages = new ArrayList<>();

    private final String PREV = "/prev";
    private final String NEXT = "/next";
    private final String GOOD_REPORT = "/goodreport";
    private final String BAD_REPORT = "/badreport";

    private final String DESIGN = "Design";

    private final String ABOUT = "О приюте";
    private final String TAKE_PET = "Как взять питомца из приюта";
    private final String BACK = "Back";
    private final String REPORT = "Прислать отчет о питомце";
    private final String ABOUT_SHELTER = "О приюте подробнее";
    private final String OPERATING_MODE = "Режим работы/Адрес";
    private final String SAFETY = "Техника безопасности";
    private final Long chatId = 123L;

    @Test
    public void testAbout() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        ReplyKeyboardMarkup replyKeyboardMarkup = mock(ReplyKeyboardMarkup.class);
        String actualText = "Приют для животных" + "\n" + "МИЛЫЕ ПУШИСТИКИ";

        when(callbackQuery.message()).thenReturn(message);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(ABOUT);
        when(buttonService.keyboardChatInfoShelterMenu()).thenReturn(replyKeyboardMarkup);

        List<SendMessage> result = handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);
        SendMessage sendMessage = result.get(0);
        String expectedText = (String) sendMessage.getParameters().get("text");

        assertEquals(1, result.size());
        verify(buttonService).keyboardChatInfoShelterMenu();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testTakePet() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        List<SendMessage> mockList = mock(List.class);

        when(callbackQuery.message()).thenReturn(message);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(TAKE_PET);
        when(commandButtonService.takePet(callbackQuery.data(), chatId, messages)).thenReturn(mockList);

        handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);
        verify(commandButtonService).takePet(callbackQuery.data(), chatId, messages);
    }

    @Test
    public void testNext() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        ArgumentCaptor<CallbackQuery> captor = ArgumentCaptor.forClass(CallbackQuery.class);

        when(callbackQuery.message()).thenReturn(message);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(NEXT);

        handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);

        verify(commandButtonService).editTakePet(captor.capture());
    }

    @Test
    public void testPrev() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        ArgumentCaptor<CallbackQuery> captor = ArgumentCaptor.forClass(CallbackQuery.class);

        when(callbackQuery.message()).thenReturn(message);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(PREV);

        handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);

        verify(commandButtonService).editTakePet(captor.capture());
    }

    @Test
    public void testReport() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        List<SendMessage> mockList = mock(List.class);

        when(callbackQuery.message()).thenReturn(message);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(REPORT);
        when(sendReportService.reportForm(chatId, messages)).thenReturn(mockList);

        handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);
        verify(sendReportService).reportForm(chatId, messages);
    }

    @Test
    public void testAboutShelter() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        Chat chat = mock(Chat.class);
        ReplyKeyboardMarkup replyKeyboardMarkup = mock(ReplyKeyboardMarkup.class);
        String shelter = "mockedString";
        String actualText = "Посмотреть каталог животных ";
        String shelterAbout = "About shelter";

        when(callbackQuery.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.username()).thenReturn(shelter);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(ABOUT_SHELTER);
        when(buttonService.viewAllTypePet()).thenReturn(replyKeyboardMarkup);
        when(shelterService.getAbout(shelter)).thenReturn(shelterAbout);

        List<SendMessage> result = handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);
        SendMessage sendMessage = result.get(1);
        String expectedText = (String) sendMessage.getParameters().get("text");

        assertEquals(2, result.size());
        verify(buttonService).viewAllTypePet();
        verify(shelterService).getAbout(shelter);
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testBack() {
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        Chat chat = mock(Chat.class);
        ReplyKeyboardMarkup replyKeyboardMarkup = mock(ReplyKeyboardMarkup.class);
        String shelter = "mockedString";
        String actualText = "Посмотреть каталог животных ";
        String shelterAbout = "About shelter";

        when(callbackQuery.message()).thenReturn(message);
        when(message.from()).thenReturn(user);
        when(user.username()).thenReturn(shelter);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(callbackQuery.data()).thenReturn(BACK);
        when(buttonService.viewAllTypePet()).thenReturn(replyKeyboardMarkup);
        when(shelterService.getAbout(shelter)).thenReturn(shelterAbout);

        List<SendMessage> result = handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);
        SendMessage sendMessage = result.get(1);
        String expectedText = (String) sendMessage.getParameters().get("text");

        assertEquals(2, result.size());
        verify(buttonService).viewAllTypePet();
        verify(shelterService).getAbout(shelter);
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testOperatingMode() {
        String path = "/path/to/photo.jpg";
        String expectedData = OPERATING_MODE;
        File photoMock = mock(File.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(callbackQuery.data()).thenReturn(expectedData);
        when(photoMock.filePath()).thenReturn(path);
        doNothing().when(commandButtonService).sendPhoto(path, chatId);

        commandButtonService.sendPhoto(path, chatId);

        verify(commandButtonService).sendPhoto(path, chatId);
        assertEquals(photoMock.filePath(), path);
        assertEquals(callbackQuery.data(), expectedData);
    }

    @Test
    public void testSafety() {
        String path = "/path/to/photo.jpg";
        String expectedData = SAFETY;
        File photoMock = mock(File.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);

        when(callbackQuery.data()).thenReturn(expectedData);
        when(photoMock.filePath()).thenReturn(path);
        doNothing().when(commandButtonService).sendPhoto(path, chatId);

        commandButtonService.sendPhoto(path, chatId);

        verify(commandButtonService).sendPhoto(path, chatId);
        verify(commandButtonService).sendPhoto(path, chatId);
        assertEquals(photoMock.filePath(), path);
        assertEquals(callbackQuery.data(), expectedData);
    }

    @Test
    public void testViewPets() {
        String data = "Dog";
        TypePet typePet = new TypePet("Dog");
        Set<TypePet> set = new HashSet<>();
        set.add(typePet);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        ReplyKeyboardMarkup replyKeyboardMarkup = mock(ReplyKeyboardMarkup.class);

        when(callbackQuery.data()).thenReturn(data);
        when(typePetService.getAllTypePet()).thenReturn(set);
        when(callbackQuery.message()).thenReturn(message);
        when(callbackQuery.message().chat()).thenReturn(chat);
        when(callbackQuery.message().chat().id()).thenReturn(chatId);
        when(buttonService.viewPets(callbackQuery.data())).thenReturn(replyKeyboardMarkup);

        handlerСalBakDataService.handlerСalBakData(callbackQuery, messages);

        verify(buttonService).viewPets(data);
    }

}
