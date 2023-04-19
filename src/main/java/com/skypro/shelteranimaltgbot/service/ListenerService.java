package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.skypro.shelteranimaltgbot.listener.TelegramBotUpdatesListener;
import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class ListenerService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private ChatSessionWithVolunteerService chatSessionService;
    private final String TYPE_PET = "Посмотреть животных";
    private final String VIEW_ALL_ANIMALS = "Посмотреть список животных";
    //TODO записать path в properties в виде переменной, вызывать через @Value
    private final String PATH_ADRESS = "src/main/resources/static/adress.jpg";


    /**
     * обработчики запросов
     */
    private final String START = "/start";
    private final String CALL_VOLUNTEER = "Позвать волонтера";
    private final String OPEN = "Принять";
    private final String CLOSE = "Закрыть/Отклонить";
    private final String ABOUT = "О приюте";
    private final String TAKE_PET = "Как взять питомца из приюта";
    private final String REPORT = "Прислать отчет о питомце";
    private final String SHARE = "Рекомендация..";
    private final String ABOUT_SHELTER = "О приюте подробнее";
    private final String OPERATING_MODE = "Режим работы/Адрес";
    private final String SAFETY = "Техника безопасности";
    @Autowired
    private ShelterService shelterService;
    @Autowired
    private PetService petService;
    @Autowired
    private TypePetService typePetService;
    private final String SAFETY_CAT = "src/main/resources/static/cat_safety.jpg";
    private final String SAFETY_DOG = "src/main/resources/static/dog_safety.jpg";


    private Long idSessionForConnect;
    private Message message;
    private Long userId;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private TypePetRepository typePetRepository;


    /**
     * метод распределяющий входящие данные от клиента
     */

    public List<SendMessage> messages(Update update) {
        List<SendMessage> messages = new ArrayList<>();
        try {
            var callBackData = update.callbackQuery();
            if (callBackData != null && update.message() == null) {
                handlerСalBakData(callBackData, messages);
            } else if (callBackData == null && update.message() != null) {
                handlerMessageData(update, messages);
            }
        } catch (NullPointerException e) {
            logger.info("не обрабатываемые данные.. ");
            e.getMessage();
        }
        return messages;
    }

    /**
     * метод обработки update.message
     */
    private List<SendMessage> handlerMessageData(Update update, List<SendMessage> messages) {
        message = update.message();
        userId = message.from().id();
        var contact = message.contact();
        if (contact != null) {
            setContact(update);
            messages.add(new SendMessage(userId, message.from().firstName() + " спасибо, мы свяжемся с вами в ближайшее время"));
            sendNotification(update, messages);

        } else {
            switch (message.text()) {
                case START:
                    mainMenu(update, messages);
                    break;
                case CALL_VOLUNTEER:
                    callVolunteer(messages);
                    break;
                case OPEN:
                    openСonnection(messages);
                    break;
                case CLOSE:
                    closeСonnection(messages);
                    break;
                default:
                    chatWithVolunteer(userId);
                    break;
            }
        }
        return messages;

    }


    /**
     * уведомление волонтерам о звонке клиенту
     */
    private List<SendMessage> sendNotification(Update update, List<SendMessage> messages) {
        String name = update.message().from().firstName() + " " + update.message().from().lastName();
        String phoneNumber = update.message().contact().phoneNumber();
        getVolunteer().stream()
                .forEach(user -> {
                    messages.add(new SendMessage(user.getUserTelegramId(), "Просьба связаться с " + name + " по номеру " + phoneNumber));
                });
        return messages;
    }


    /**
     * закрытие соединения с клиентом
     */

    private List<SendMessage> closeСonnection(List<SendMessage> messages) {
        chatSessionService.getChatSessionForClose(idSessionForConnect, SessionEnum.CLOSE);
        ChatSessionWithVolunteer chatUser = chatSessionService.getChatUser(idSessionForConnect);
        messages.add(new SendMessage(chatUser.getTelegramIdUser(), "Волонтер перевел Вас на бота, для повторной связи с волонтером нажмите кнопку Позвать волонтера"));
        return messages;
    }


    /**
     * чат с волонтером
     */
    private void chatWithVolunteer(Long userId) {
        ChatSessionWithVolunteer session = chatSessionService.getSession(userId);
        idSessionForConnect = session.getId();
        Long userTelegramId = session.getTelegramIdUser();
        Long volunteerChatId = session.getTelegramIdVolunteer();


        if (chatSessionService.checkSession(idSessionForConnect) && !userId.equals(userTelegramId)) {
            ForwardMessage forwardMessage = new ForwardMessage(userTelegramId, userId, message.messageId());
            SendResponse response = telegramBot.execute(forwardMessage);
        } else if (chatSessionService.checkSession(idSessionForConnect) && !userId.equals(volunteerChatId)) {
            ForwardMessage forwardMessage = new ForwardMessage(volunteerChatId, userId, message.messageId());
            SendResponse response = telegramBot.execute(forwardMessage);
        }

//        idSessionForConnect = chatSessionService.getSessionId(userId);
//        Long userTelegramId = chatSessionService.getChatUser(idSessionForConnect).getTelegramIdUser();
//        Long volunteerChatId = chatSessionService.getChatUser(idSessionForConnect).getTelegramIdVolunteer();
//        if (chatSessionService.checkSession(idSessionForConnect) && !chatId.equals(userTelegramId)) {
//            ForwardMessage forwardMessage = new ForwardMessage(userTelegramId, chatId, message.messageId());
//            SendResponse response = telegramBot.execute(forwardMessage);
//        } else if (chatSessionService.checkSession(idSessionForConnect) && !chatId.equals(volunteerChatId)) {
//            ForwardMessage forwardMessage = new ForwardMessage(volunteerChatId, chatId, message.messageId());
//            SendResponse response = telegramBot.execute(forwardMessage);
//        }
    }


    /**
     * отправка фото
     */
    private void sendPhoto(String path, Long chatId) {
        File photo = new File(path);
        SendPhoto sendPhoto = new SendPhoto(chatId, photo);
        telegramBot.execute(sendPhoto);
    }

    /**
     * подтверждение соединения волонтером с клиентом
     */

    private List<SendMessage> openСonnection(List<SendMessage> messages) {
        if (!chatSessionService.checkSession(idSessionForConnect)) {
            chatSessionService.getChatSessionForClose(idSessionForConnect, SessionEnum.OPEN);
        } else {
            messages.add(new SendMessage(userId, "Запрос от пользователя обрабатывается, либо уже закрыт"));
        }
        return messages;
    }


    /**
     * метод направляем волонтерам запрос от клиента
     */
    private List<SendMessage> callVolunteer(List<SendMessage> messages) {
        getVolunteer().stream()
                .forEach(user -> {
                    //отправили сообщение всем волонтерам и кнопки принять / откланить, открыли сессию в статусе ожидания
                    messages.add(new SendMessage(user.getUserTelegramId(), "нужна помощь " + " для " + message.from().firstName()).replyMarkup(keyboardForChatSession()));
                    ChatSessionWithVolunteer newSession = new ChatSessionWithVolunteer(user.getUserTelegramId(), message.from().id(), SessionEnum.STANDBY);
                    chatSessionService.createSession(newSession);
                    //idSessionForConnect = newSession.getId();
                });
        messages.add(new SendMessage(userId, "Соединение устанавливается.."));
        return messages;
    }


    /**
     * вывод основного меню
     */
    private List<SendMessage> mainMenu(Update update, List<SendMessage> messages) {
        User user = new User(message.from().firstName(), message.from().lastName(), message.from().id(), userId, StatusEnum.GUEST, RoleEnum.USER);
        userService.addUser(user);
        messages.add(new SendMessage(userId, "Привет " + user.getFirstName()).replyMarkup(keyboardMenu()));
        messages.add(new SendMessage(userId, "Выберете пункт меню:").replyMarkup(keyboardChatMenu()));
        return messages;
    }


    /**
     * метод обработки update.callBack
     */
    private List<SendMessage> handlerСalBakData(CallbackQuery callBackData, List<SendMessage> messages) {
        var chatIdFromCallBackData = callBackData.message().chat().id();
        switch (callBackData.data()) {
            case ABOUT:
                messages.add(new SendMessage(chatIdFromCallBackData, "Здравствуйте!" + "\n" + "Выберете пункт меню:").replyMarkup(keyboardChatInfoShelterMenu()));
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
                messages.add(new SendMessage(chatIdFromCallBackData, "Посмотреть каталог животных ").replyMarkup(viewAllTypePet()));
                break;
            case OPERATING_MODE:
                sendPhoto(PATH_ADRESS, chatIdFromCallBackData);
                break;
            case SAFETY:
                sendPhoto(SAFETY_CAT, chatIdFromCallBackData);
                sendPhoto(SAFETY_DOG, chatIdFromCallBackData);
                break;
            case VIEW_ALL_ANIMALS:
                break;
            default:
                if (checkCallbackDataTypePet(callBackData.data())) {
                    messages.add(new SendMessage(chatIdFromCallBackData, callBackData.data()).replyMarkup(viewPets(callBackData.data())));
                } else if (checkCallbackDataPet(callBackData.data())) {
                    messages.add(new SendMessage(chatIdFromCallBackData, "в разработке"));
                    viewInfoAboutPet(callBackData.data());
                }
                break;
        }
        return messages;
    }

    private void viewInfoAboutPet(String data) {

    }


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
     * метод выводит всех питомцев по выбранному типу
     **/
    private Keyboard viewPets(String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<Pet> pets = new ArrayList<>(petService.getAllPetByTypePet(data));
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
     * метод выводит все виды животных(кошки собаки и тд)
     */
    private Keyboard viewAllTypePet() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        Set<TypePet> typePetsList = new HashSet<>(typePetService.getAllTypePet());
        for (TypePet typePet : typePetsList) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(typePet.getType()).callbackData(typePet.getType()));
        }

        return inlineKeyboardMarkup;

    }


    /**
     * добавляем номер телефона в БД
     */
    private void setContact(Update update) {
        userService.setContact(update);
    }


    /**
     * получаем список волонтеров
     */
    public List<User> getVolunteer() {
        return new ArrayList<>(userService.checkUsersByRole(RoleEnum.VOLUNTEER));
    }


    /**
     * выпадающее меню в чат
     */
    private Keyboard keyboardChatMenu() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(ABOUT).callbackData(ABOUT))
                .addRow(new InlineKeyboardButton(TAKE_PET).callbackData(TAKE_PET))
                .addRow(new InlineKeyboardButton(REPORT).callbackData(REPORT))
                .addRow(new InlineKeyboardButton(SHARE).switchInlineQuery(SHARE));
    }

    /**
     * выпадающее меню в чат
     * подробнее о приюте
     */
    private Keyboard keyboardChatInfoShelterMenu() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(ABOUT_SHELTER).callbackData(ABOUT_SHELTER))
                .addRow(new InlineKeyboardButton(OPERATING_MODE).callbackData(OPERATING_MODE))
                .addRow(new InlineKeyboardButton(SAFETY).callbackData(SAFETY));
    }

    /**
     * нижнее меню
     */
    private Keyboard keyboardMenu() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton(CALL_VOLUNTEER),
                new KeyboardButton("Оставить контакт").requestContact(true))
                .addRow(new KeyboardButton(START)).resizeKeyboard(true);
    }


    /**
     * кнопки принять или отклонить запрос для волонтера
     * */
    private Keyboard keyboardForChatSession() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton(OPEN),
                new KeyboardButton(CLOSE)).resizeKeyboard(true);
    }


}