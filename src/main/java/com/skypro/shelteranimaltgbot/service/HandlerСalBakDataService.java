package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    TakePetFromShelterService takePetFromShelterService;

    private final String ABOUT = "О приюте";
    private final String TAKE_PET = "Как взять питомца из приюта";
    private final String NEXT = "/next";
    private final String BACK = "/back";
    private final String REPORT = "Прислать отчет о питомце";
    private final String ABOUT_SHELTER = "О приюте подробнее";
    private final String OPERATING_MODE = "Режим работы/Адрес";
    private final String SAFETY = "Техника безопасности";
    private final String RULES_OF_ACQUAINTANCE = "Правила знакомства";
    private final String DOCUMENTS = "Список документов для усыновления";
    private final String TRANSPORT_RECOMMENDATIONS = "Рекомендации по транспортировке";
    private final String RECOMMENDATIONS_FOR_KID_HOUSE = "Обустройство дома для щенка/котенка";
    private final String RECOMMENDATIONS_FOR_ADULT_HOUSE = "Обустройство дома для взрослого животного";
    private final String RECOMMENDATIONS_FOR_INVALID_HOUSE = "Обустройство дома для животного с ограниченными возможностями";
    private final String REASONS_FOR_REJECTION = "Причины отказа в усыновлении животного";
    private final String CYNOLOGIST_ADVICE = "Советы кинолога";
    private final String BEST_CYNOLOGISTS = "Проверенные кинологи";
    private final String TAKE_CAT = "Взять кошку";
    private final String TAKE_DOG = "Взять собаку";
    private final String VIEW_ALL_ANIMALS = "Посмотреть список животных";
    private final String SAFETY_CAT = "src/main/resources/static/cat_safety.jpg";
    private final String SAFETY_DOG = "src/main/resources/static/dog_safety.jpg";
    private final String PATH_ADRESS = "src/main/resources/static/adress.jpg";


    public List<SendMessage> handlerСalBakData(CallbackQuery callBackData, List<SendMessage> messages) {
        Long chatIdFromCallBackData = callBackData.message().chat().id();
        switch (callBackData.data()) {
            case ABOUT:
                messages.add(new SendMessage(chatIdFromCallBackData, "Приют для животных" + "\n" + "МИЛЫЕ ПУШИСТИКИ").replyMarkup(buttonService.keyboardChatInfoShelterMenu()));
                break;
            case TAKE_PET:
            case NEXT:
            case BACK:
                //TODO создать Entity класс takePetFromShelter с полями id (автоматическое заполнение), поле text с описанием как взять животное, добавить в insert_into.sql заполнение таблицы
                //TODO создать Service и Repository класса takePetFromShelter
                //TODO сделать метод обработки запроса как взять питомца (получение из репозитория информации и вывод ее в чат)
                commandButtonService.takePet(callBackData.data(), chatIdFromCallBackData, messages);

                // messages.add(new SendMessage(chatIdFromCallBackData, "Здравствуйте, выберите:").replyMarkup(buttonService.takePetMenu()));

                //messages.add(new SendMessage(chatIdFromCallBackData, commandButtonService.takePet()));
                break;
//            case TAKE_CAT:
//                messages.add(new SendMessage(chatIdFromCallBackData, "Вас интересует кошка:").replyMarkup(buttonService.takeCatMenu()));
//                break;
//            case TAKE_DOG:
//                messages.add(new SendMessage(chatIdFromCallBackData, "Вас интересует собака:"));//.replyMarkup(buttonService.keyboardGetCatRecommendation()));
//                break;
//            case RULES_OF_ACQUAINTANCE:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(1L)));
//                break;
//            case TRANSPORT_RECOMMENDATIONS:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(2L)));
//                break;
//            case RECOMMENDATIONS_FOR_KID_HOUSE:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(3L)));
//                break;
//            case RECOMMENDATIONS_FOR_ADULT_HOUSE:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(4L)));
//                break;
//            case RECOMMENDATIONS_FOR_INVALID_HOUSE:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(5L)));
//                break;
//            case REASONS_FOR_REJECTION:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(6L)));
//                break;
//            case CYNOLOGIST_ADVICE:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(7L)));
//                break;
//            case BEST_CYNOLOGISTS:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(8L)));
//                break;
//            case DOCUMENTS:
//                messages.add(new SendMessage(chatIdFromCallBackData, takePetFromShelterService.getDescription(2L)));
//                break;
            case REPORT:
                //TODO сделать метод по обработке запроса подать отчет, создать Entity класс Report в соответствии с таблицей БД + добавить поле Pet (после оформления должен измениться статус Pet на BUSY, обновление статуса должно быть реализовано через контроллер)

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
        //TODO доработать метод, вывести его фотографию (фото с хранилища "/petPhoto" не из БД!), вывести всю информацию о животном, + 1) кнопку взять/оформить питомца 2) вновь показать список питомцев(?)
    }


    /**
     * метод проверяет если выбор животного то возврат true
     */
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
