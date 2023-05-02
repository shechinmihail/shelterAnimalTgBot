package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import com.skypro.shelteranimaltgbot.model.TakePetFromShelter;
import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class CommandButtonService {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private ChatSessionWithVolunteerService chatSessionService;

    @Autowired
    ButtonService buttonService;

    @Autowired
    UserService userService;

    @Autowired
    TypePetService typePetService;

    private Message message;
    private Long userId;
    private Integer rule = 1;
    private final Integer SIZE = 1;
    private final String NEXT = "/next";
    private final String BACK = "/back";

    @Autowired
    private TypePetRepository typePetRepository;


    /**
     * метод выводит основное меню после нажатия start
     */
    public List<SendMessage> mainMenu(Update update, List<SendMessage> messages) {
        message = update.message();
        userId = message.from().id();
        User user = new User(message.from().firstName(), message.from().lastName(), message.from().id(), StatusEnum.GUEST, RoleEnum.USER);
        userService.addUser(user);
        messages.add(new SendMessage(userId, "Здравствуйте " + user.getFirstName()).replyMarkup(buttonService.keyboardMenu()));
        messages.add(new SendMessage(userId, "Выберете пункт меню:").replyMarkup(buttonService.keyboardChatMenu()));
        return messages;
    }


    /**
     * метод направляем волонтерам запрос от клиента
     */
    public List<SendMessage> callVolunteer(Update update, List<SendMessage> messages) {
        message = update.message();
        userId = message.from().id();
        try {
            getFreeVolunteer().stream()
                    .forEach(user -> {
                        //отправили сообщение всем волонтерам и кнопки принять / отклонить, открыли сессию в статусе ожидания
                        messages.add(new SendMessage(user.getUserTelegramId(), "нужна помощь " + " для " + message.from().firstName()).replyMarkup(buttonService.keyboardForChatSession()));
                        ChatSessionWithVolunteer newSession = new ChatSessionWithVolunteer(user.getUserTelegramId(), message.from().id(), SessionEnum.STANDBY);
                        chatSessionService.createSession(newSession);
                    });
            messages.add(new SendMessage(userId, "Соединение устанавливается.."));
        } catch (NullPointerException e) {
            e.getMessage();
            messages.add(new SendMessage(userId, "Пока все волонтеры заняты "));
        }
        return messages;
    }


    /**
     * получаем список волонтеров
     */
    public List<SendMessage> openСonnection(List<SendMessage> messages) {
        Long idSessionForConnect = chatSessionService.getLastId(userId);
        if (!chatSessionService.checkSession(idSessionForConnect)) {
            chatSessionService.getChatSessionForReplaceStatus(idSessionForConnect, SessionEnum.OPEN);
        } else {
            messages.add(new SendMessage(userId, "Запрос от пользователя обрабатывается, либо уже закрыт"));
        }
        return messages;
    }


    /**
     * закрытие соединения с клиентом
     */

    public List<SendMessage> closeСonnection(Update update, List<SendMessage> messages) {
        message = update.message();
        userId = message.from().id();
        Long idSessionForConnect = chatSessionService.getLastId(userId);
        chatSessionService.getChatSessionForReplaceStatus(idSessionForConnect, SessionEnum.CLOSE);
        ChatSessionWithVolunteer chatUser = chatSessionService.getChatUser(idSessionForConnect);
        messages.add(new SendMessage(chatUser.getTelegramIdUser(), "Волонтер перевел Вас на бота, для повторной связи с волонтером нажмите кнопку Позвать волонтера"));
        return messages;
    }


    /**
     * метод получения всех свободных волонтеров, если нет свободных волонтеров включить режим ожидания
     * */
    public List<User> getFreeVolunteer(){
        Set<ChatSessionWithVolunteer> sessions = new HashSet<>(
                chatSessionService.getAllSession()
                        .stream()
                        .filter(chatSessionWithVolunteer -> chatSessionWithVolunteer.getSession() == SessionEnum.OPEN).toList()

        );

        Set<Long> idFreeVolunteer = new HashSet<>();
        sessions.stream()
                .forEach(session -> {
                    idFreeVolunteer.add(session.getTelegramIdVolunteer());
                });

        if (sessions.size() != 0) {
            List<User> freeVolunteerList = new ArrayList<>();
            for (User u : getAllVolunteer()) {
                if (!idFreeVolunteer.contains(u.getUserTelegramId())) {
                    freeVolunteerList.add(u);
                }
            }
            return freeVolunteerList;
        } else {
            return getAllVolunteer();
        }


    }


    /**
     * получение всех волонтеров
     * */
    private List<User> getAllVolunteer() {
        return new ArrayList<>(userService.checkUsersByRole(RoleEnum.VOLUNTEER));
    }


    /**
     * отправка фото
     */
    public void sendPhoto(String path, Long chatId) {
        File photo = new File(path);
        SendPhoto sendPhoto = new SendPhoto(chatId, photo);
        telegramBot.execute(sendPhoto);
    }


    /**
     * метод обновляет таблицу users, добавляет номер телефона
     * */
    public void setContact(Update update) {
        userService.setContact(update);
    }


    /**
     * метод перенаправляет запрос от гостя волонтеру с просьбой связаться с ним по телефону
     *
     * */
    public List<SendMessage> sendNotification(Update update, List<SendMessage> messages) {
        String name = update.message().from().firstName() + " " + update.message().from().lastName();
        String phoneNumber = update.message().contact().phoneNumber();
        getFreeVolunteer().stream()
                .forEach(user -> {
                    messages.add(new SendMessage(user.getUserTelegramId(), "Просьба связаться с " + name + " по номеру " + phoneNumber));
                });
        return messages;
    }

    /**
     * получаем первое сообщение с правилами "Как взять питомца из приюта"
     */
    public List<SendMessage> takePet(String text, Long id, List<SendMessage> messages) {
        messages.add(new SendMessage(id, returnedText(1)).parseMode(ParseMode.HTML).replyMarkup(buttonService.paginationButton()));
        return messages;
    }

    /**
     * редактируем сообщение, действие после нажатия кнопок /next, /back
     */
    public void editTakePet(CallbackQuery callbackData) {
        EditMessageText editMessageText = null;
        Long chatId = callbackData.message().chat().id();
        Integer messageId = callbackData.message().messageId();
        String text = callbackData.data();
        if (text.equals(NEXT)) {
            rule = rulePage(text);
            editMessageText = new EditMessageText(chatId, messageId, returnedText(rule)).parseMode(ParseMode.HTML).replyMarkup(buttonService.paginationButton());
        } else if (text.equals(BACK)) {
            rule = rulePage(text);
            editMessageText = new EditMessageText(chatId, messageId, returnedText(rule)).parseMode(ParseMode.HTML).replyMarkup(buttonService.paginationButton());
        }

        telegramBot.execute(editMessageText);

    }

    /**
     * составляем текст сообщения "Как взять питомца из приюта" при помощи StringBuilder с запросом в БД также добавлена пагинация страниц
     */
    private String returnedText(Integer i) {
        StringBuilder takePetsRule = new StringBuilder();
        List<TypePet> pets = typePetService.findAllByPagination(i, SIZE);
        pets.stream()
                .forEach(typePet -> {
                    takePetsRule.append("<b>" + typePet.getType() + "</b>" + "\n\n");
                    typePet.getTakePetFromShelters().stream()
                            .sorted(Comparator.comparing(TakePetFromShelter::getId))
                            .forEach(takePetFromShelter -> {
                                takePetsRule.append("<b>" + takePetFromShelter.getDescription() + "</b>" + "\n" + "<i>" + takePetFromShelter.getNameRule() + "</i>" + "\n");
                            });
                });
        return takePetsRule.toString();
    }

    /**
     * определяем страницу
     */
    private Integer rulePage(String text) {
        if (text != null && text.equals(NEXT) && rule < typePetRepository.findAll().size()) {
            rule++;
        } else if (text != null && text.equals("/back") && rule > 1) {
            rule--;
        }
        return rule;
    }


}
