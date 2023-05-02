package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.request.*;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static com.skypro.shelteranimaltgbot.model.Enum.CommandButton.*;

@Service
public class ButtonService {

    @Autowired
    PetService petService;

    @Autowired
    TypePetService typePetService;


    /**
     * основное меню + позвать волонтера, поделится контактом
     */

    public Keyboard keyboardMenu() {

        return new ReplyKeyboardMarkup(
                new KeyboardButton(CALL_VOLUNTEER.getCommandText()),
                new KeyboardButton("Оставить контакт").requestContact(true))
                .addRow(new KeyboardButton(START.getCommandText())).resizeKeyboard(true);
    }

    /**
     * выпадающее меню в чат
     */
    public Keyboard keyboardChatMenu() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(ABOUT.getCommandText()).callbackData(ABOUT.getCommandText()))
                .addRow(new InlineKeyboardButton(TAKE_PET.getCommandText()).callbackData(TAKE_PET.getCommandText()))
                .addRow(new InlineKeyboardButton(REPORT.getCommandText()).callbackData(REPORT.getCommandText()))
                .addRow(new InlineKeyboardButton(SHARE.getCommandText()).switchInlineQuery(SHARE.getCommandText()));
    }

    /**
     * кнопки принять или отклонить запрос для волонтера
     * */
    public Keyboard keyboardForChatSession() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton(OPEN.getCommandText()),
                new KeyboardButton(CLOSE.getCommandText())).resizeKeyboard(true);
    }

    /**
     * метод выводит все виды животных(кошки собаки и тд)
     */

    public Keyboard viewAllTypePet() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Set<TypePet> typePetsList = new HashSet<>(typePetService.getAllTypePet());
        for (TypePet typePet : typePetsList) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(typePet.getType()).callbackData(typePet.getType()));
        }
        return inlineKeyboardMarkup;
    }

    /**
     * выпадающее меню в чат
     * подробнее о приюте
     */
    public Keyboard keyboardChatInfoShelterMenu() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(ABOUT_SHELTER.getCommandText()).callbackData(ABOUT_SHELTER.getCommandText()))
                .addRow(new InlineKeyboardButton(OPERATING_MODE.getCommandText()).callbackData(OPERATING_MODE.getCommandText()))
                .addRow(new InlineKeyboardButton(SAFETY.getCommandText()).callbackData(SAFETY.getCommandText()));
    }

    /**
     * метод возвращает всех животных по типу (кошки / собаки)
     * */
    public Keyboard viewPets(String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Set<Pet> pets = new HashSet<>(petService.getAllPetByTypePet(data));
        pets.stream()
                .filter(p -> p.getTypePet().getType().equals(data))
                .sorted(Comparator.comparing(Pet::getName))
                .forEach(pet -> {
                    inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Имя " + pet.getName() + " Возраст " + pet.getAge() + " года")
                            .callbackData(pet.getId() + " " + pet.getName() + " " + pet.getAge()));
                });
        return inlineKeyboardMarkup;
    }

    /**
     * кнопки получить для перелистывания страниц
     */
    public InlineKeyboardMarkup paginationButton() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("<--назад").callbackData("/back"),
                new InlineKeyboardButton("вперед-->").callbackData("/next")
        );
    }


}
