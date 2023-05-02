package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.model.User;
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

    private final String DESIGN = "Design";

    private final String ABOUT = "О приюте";
    private final String TAKE_PET = "Как взять питомца из приюта";
    private final String NEXT = "/next";
    private final String BACK = "/back";
    private final String REPORT = "Прислать отчет о питомце";
    private final String ABOUT_SHELTER = "О приюте подробнее";
    private final String OPERATING_MODE = "Режим работы/Адрес";
    private final String SAFETY = "Техника безопасности";
    private final String VIEW_ALL_ANIMALS = "Посмотреть список животных";
    private final String BACK_ON = "Back";
    @Autowired
    UserService userService;
    private final String SAFETY_CAT = "src/main/resources/static/cat_safety.jpg";
    private final String SAFETY_DOG = "src/main/resources/static/dog_safety.jpg";
    private final String PATH_ADRESS = "src/main/resources/static/adress.jpg";

    public List<SendMessage> handlerСalBakData(CallbackQuery callBackData, List<SendMessage> messages) {
        Long chatIdFromCallBackData = callBackData.message().chat().id();
        switch (callBackData.data()) {
            case ABOUT, BACK_ON:
                messages.add(new SendMessage(chatIdFromCallBackData, "Приют для животных" + "\n" + "МИЛЫЕ ПУШИСТИКИ").replyMarkup(buttonService.keyboardChatInfoShelterMenu()));
                break;
            case TAKE_PET:
                commandButtonService.takePet(callBackData.data(), chatIdFromCallBackData, messages);
            case NEXT:
            case BACK:
                commandButtonService.editTakePet(callBackData);
                break;
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
                    sendPetPhoto(callBackData.data(), chatIdFromCallBackData);
                    messages.add(viewPetInfo(chatIdFromCallBackData, callBackData));
                } else if (checkCallBackDataDesign(callBackData.data())) {// если нажата кнопка оформить
                    String[] callBack = callBackData.data().split(" ");
                    Long petId = Long.valueOf(callBack[1]);
                    String petName = callBack[2];
                    messages.add(new SendMessage(chatIdFromCallBackData, petService.findPet(petId).getTypePet().getDocumentsList().toString()));
                    // отправили Пользователю список документов
                    List<User> volunteers = userService.checkUsersByRole(RoleEnum.VOLUNTEER);
                    for (User user : volunteers) { // отправили всем волонтерам уведомление что животное, хотят забрать
                        messages.add(new SendMessage(user.getUserTelegramId(), userService.findUser(chatIdFromCallBackData).getFirstName() + " хочет оформить " + petName));
                    }
                }
                break;
        }
        return messages;
    }

    /**
     * метод отправки фотографии животного
     * берет ссылку из БД, а ссылка указывает на локальный адрес фото
     */
    private void sendPetPhoto(String data, Long chatId) {
        String[] callBack = data.split(" ");
        Pet pet = petService.findPet(Long.valueOf(callBack[0]));
        String pathOfPhoto = pet.getFilePath();
        commandButtonService.sendPhoto(pathOfPhoto, chatId);
    }

    /**
     * метод предоставления информации о животном
     * и вывода кнопок: "Оформить" и "Назад"
     */
    private SendMessage viewPetInfo(Long chatId, CallbackQuery callbackQuery) {
        String[] callBack = callbackQuery.data().split(" ");
        String petInfo = callBack[1] + " возраст: " + callBack[2];
        SendMessage sendMessage = new SendMessage(chatId, petInfo);
        sendMessage.replyMarkup(buttonService.designOrBack(callbackQuery));
        return sendMessage;
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

    /**
     * метод проверяет если нажата кнопка оформить то возврат true
     */
    private boolean checkCallBackDataDesign(String data) {
        String[] dataSplit = data.split(" ");
        String string = dataSplit[0];
        return string.equals(DESIGN);
    }


}
