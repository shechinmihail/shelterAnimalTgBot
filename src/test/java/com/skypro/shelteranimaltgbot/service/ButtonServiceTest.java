package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.request.*;
import com.skypro.shelteranimaltgbot.model.Enum.StatusPet;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.skypro.shelteranimaltgbot.model.Enum.CommandButton.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ButtonServiceTest {

    @Mock
    TypePetRepository typePetRepository;

    @Mock
    TypePetService typePetService;

    @Mock
    PetService petService;

    @InjectMocks
    ButtonService buttonService;


    // @InjectMocks


    @Test
    void keyboardMenu() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(CALL_VOLUNTEER.getCommandText()),
                new KeyboardButton("Оставить контакт").requestContact(true))
                .addRow(new KeyboardButton(START.getCommandText())).resizeKeyboard(true);
        Assertions.assertEquals(replyKeyboardMarkup.getClass(), buttonService.keyboardMenu().getClass());
    }


    @Test
    void keyboardForChatSession() {
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(OPEN.getCommandText()),
                new KeyboardButton(CLOSE.getCommandText())).resizeKeyboard(true);
        System.out.println(replyKeyboardMarkup.hashCode());
        Assertions.assertEquals(replyKeyboardMarkup.getClass(), buttonService.keyboardForChatSession().getClass());

    }

    @Test
    void keyboardChatMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(ABOUT.getCommandText()).callbackData(ABOUT.getCommandText()))
                .addRow(new InlineKeyboardButton(TAKE_PET.getCommandText()).callbackData(TAKE_PET.getCommandText()))
                .addRow(new InlineKeyboardButton(REPORT.getCommandText()).callbackData(REPORT.getCommandText()))
                .addRow(new InlineKeyboardButton(SHARE.getCommandText()).switchInlineQuery(SHARE.getCommandText()));
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.keyboardChatMenu());
    }

    @Test
    void viewAllTypePet() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<TypePet> typePetsList = new ArrayList<>();
        typePetsList.add(new TypePet("Кошки"));
        typePetsList.add(new TypePet("Собаки"));
        for (TypePet typePet : typePetsList) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(typePet.getType()).callbackData(typePet.getType()));
        }

        when(typePetService.getAllTypePet()).thenReturn(typePetsList);
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.viewAllTypePet());
    }

    @Test
    void keyboardChatInfoShelterMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(ABOUT_SHELTER.getCommandText()).callbackData(ABOUT_SHELTER.getCommandText()))
                .addRow(new InlineKeyboardButton(OPERATING_MODE.getCommandText()).callbackData(OPERATING_MODE.getCommandText()))
                .addRow(new InlineKeyboardButton(SAFETY.getCommandText()).callbackData(SAFETY.getCommandText()));
        Assertions.assertEquals(inlineKeyboardMarkup, buttonService.keyboardChatInfoShelterMenu());
    }

    @Test
    void viewPets() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet("Шарик", 2, new TypePet("Собаки", null), StatusPet.FREE));
        pets.stream()
                .sorted(Comparator.comparing(Pet::getName))
                .forEach(pet -> {
                    inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Имя " + pet.getName() + " Возраст " + pet.getAge() + " года")
                            .callbackData(pet.getId() + " " + pet.getName() + " " + pet.getAge()));
                });
        when(petService.getAllPetByTypePet("Собаки")).thenReturn(pets);
        Assertions.assertEquals(inlineKeyboardMarkup.getClass(), buttonService.viewPets("Собаки").getClass());

    }
}