package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.request.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.skypro.shelteranimaltgbot.model.Enum.CommandButton.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ButtonServiceTest {

    @Mock
    ButtonService buttonService;

    @InjectMocks
    TypePetService typePetService;


    //TODO Посмотреть что не так показывает 0% по тестам
    @Test
    void keyboardMenu() {
        KeyboardButton keyboardButton1 = new KeyboardButton(CALL_VOLUNTEER.getCommandText());
        KeyboardButton keyboardButton2 = new KeyboardButton("Оставить контакт").requestContact(true);
        KeyboardButton keyboardButton3 = new KeyboardButton(START.getCommandText()).requestContact(true);
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton1, keyboardButton2).addRow(keyboardButton3);
        Mockito.when(buttonService.keyboardMenu()).thenReturn(replyKeyboardMarkup);
        Assertions.assertEquals(replyKeyboardMarkup, buttonService.keyboardMenu());
    }

    @Test
    void keyboardChatMenu() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(ABOUT.getCommandText()).callbackData(ABOUT.getCommandText());
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(TAKE_PET.getCommandText()).callbackData(TAKE_PET.getCommandText());
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(REPORT.getCommandText()).callbackData(REPORT.getCommandText());
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton(SHARE.getCommandText()).callbackData(SHARE.getCommandText());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardButton);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton1);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton2);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton3);
        when(buttonService.keyboardChatMenu()).thenReturn(inlineKeyboardMarkup);
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.keyboardChatMenu());


    }

    @Test
    void keyboardForChatSession() {
        KeyboardButton keyboardButton1 = new KeyboardButton(OPEN.getCommandText());
        KeyboardButton keyboardButton2 = new KeyboardButton(CLOSE.getCommandText());
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton1, keyboardButton2);
        when(buttonService.keyboardForChatSession()).thenReturn(replyKeyboardMarkup);
        Assertions.assertEquals(replyKeyboardMarkup, buttonService.keyboardForChatSession());

    }

    @Test
    void viewAllTypePet() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Кошки");
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("Собаки");
        inlineKeyboardMarkup.addRow(inlineKeyboardButton);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton1);
        when(buttonService.viewAllTypePet()).thenReturn(inlineKeyboardMarkup);
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.viewAllTypePet());
    }

    @Test
    void keyboardChatInfoShelterMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(ABOUT_SHELTER.getCommandText()).callbackData(ABOUT_SHELTER.getCommandText());
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(OPERATING_MODE.getCommandText()).callbackData(OPERATING_MODE.getCommandText());
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(SAFETY.getCommandText()).callbackData(OPERATING_MODE.getCommandText());
        inlineKeyboardMarkup.addRow(inlineKeyboardButton);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton1);
        inlineKeyboardMarkup.addRow(inlineKeyboardButton2);
        when(buttonService.keyboardChatInfoShelterMenu()).thenReturn(inlineKeyboardMarkup);
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.keyboardChatInfoShelterMenu());
    }

    @Test
    void viewPets() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Имя Шарик возраст 2 года ").callbackData("1 Шарик 2");
        inlineKeyboardMarkup.addRow(inlineKeyboardButton);
        when(buttonService.viewPets("Собаки")).thenReturn(inlineKeyboardMarkup);
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.viewPets("Собаки"));

    }
}