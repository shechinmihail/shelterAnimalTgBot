package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HandlerСalBakDataService {
    @Autowired
    private ButtonService buttonService;

    @Autowired
    private TypePetService typePetService;

    @Autowired
    private PetService petService;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    ShelterService shelterService;

    @Autowired
    CommandButtonService commandButtonService;

    private final String ABOUT = "О приюте";
    private final String TAKE_PET = "Как взять питомца из приюта";
    private final String REPORT = "Прислать отчет о питомце";
    private final String ABOUT_SHELTER = "О приюте подробнее";
    private final String OPERATING_MODE = "Режим работы/Адрес";
    private final String SAFETY = "Техника безопасности";
    private final String VIEW_ALL_ANIMALS = "Посмотреть список животных";
    private final String SAFETY_CAT = "src/main/resources/static/cat_safety.jpg";
    private final String SAFETY_DOG = "src/main/resources/static/dog_safety.jpg";
    private final String PATH_ADRESS = "src/main/resources/static/adress.jpg";


    public List<SendMessage> handlerСalBakData(CallbackQuery callBackData, List<SendMessage> messages) {
        Long chatIdFromCallBackData = callBackData.message().chat().id();
        switch (callBackData.data()) {
            case ABOUT:
                messages.add(new SendMessage(chatIdFromCallBackData, "Милые пушитсики").replyMarkup(buttonService.keyboardChatInfoShelterMenu()));
                break;
            case TAKE_PET:
                //TODO сделать метод обработки запроса как взять питомца
                messages.add(new SendMessage(chatIdFromCallBackData, "в разработке"));
                break;
            case REPORT:
                //TODO сделать метод по обработке запроса подать отчет
                messages.add(new SendMessage(chatIdFromCallBackData, "в разработке"));
                break;
            case ABOUT_SHELTER:
                String shelter = callBackData.message().from().username();
                messages.add(new SendMessage(chatIdFromCallBackData, shelterService.getAbout(shelter)));
                messages.add(new SendMessage(chatIdFromCallBackData, "Посмотреть каталог животных ").replyMarkup(buttonService.viewAllTypePet()));
                break;
            case OPERATING_MODE:
                commandButtonService.sendPhoto(PATH_ADRESS, chatIdFromCallBackData);
                break;
            case SAFETY:
                commandButtonService.sendPhoto(SAFETY_CAT, chatIdFromCallBackData);
                commandButtonService.sendPhoto(SAFETY_DOG, chatIdFromCallBackData);
                break;
            case VIEW_ALL_ANIMALS:
                break;
            default:
                if (checkCallbackDataTypePet(callBackData.data())) {
                    messages.add(new SendMessage(chatIdFromCallBackData, callBackData.data()).replyMarkup(buttonService.viewPets(callBackData.data())));
                } else if (checkCallbackDataPet(callBackData.data())) {
                    messages.add(new SendMessage(chatIdFromCallBackData, "в разработке"));
                    viewInfoAboutPet(callBackData.data());
                }
                break;
        }
        return messages;
    }



    private void viewInfoAboutPet(String data) {
    //TODO доработать метод
    }


     /**
     * метод проверяет
     * */
    private boolean checkCallbackDataPet(String data) {
        String[] dataSplit = data.split(" ");
        Pet pet = petService.findPet(Long.valueOf(dataSplit[0]));
        return pet.getName().equals(dataSplit[1]) && pet.getAge() == Integer.valueOf(dataSplit[2]);
    }

    /**
     * метод проверяет если выбор типа животного то возврат true
     */
    private boolean checkCallbackDataTypePet(String data) {
        Set<TypePet> typePets = new HashSet<>(typePetService.getAllTypePet());
        return typePets.stream().anyMatch(typePet -> {
            return typePet.getType().equals(data);
        });
    }




}
