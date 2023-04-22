package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import com.skypro.shelteranimaltgbot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private Message message;
    private Long userId;




    /**
     * метод выводит основное меню после нажатия start
     * */
    public  List<SendMessage> mainMenu(Update update, List<SendMessage> messages) {
        message = update.message();
        userId = message.from().id();
        User user = new User(message.from().firstName(), message.from().lastName(), message.from().id(), userId, StatusEnum.GUEST, RoleEnum.USER);
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
        getVolunteer().stream()
                .forEach(user -> {
                    //отправили сообщение всем волонтерам и кнопки принять / отклонить, открыли сессию в статусе ожидания
                    messages.add(new SendMessage(user.getUserTelegramId(), "нужна помощь " + " для " + message.from().firstName()).replyMarkup(buttonService.keyboardForChatSession()));
                    ChatSessionWithVolunteer newSession = new ChatSessionWithVolunteer(user.getUserTelegramId(), message.from().id(), SessionEnum.STANDBY);
                    chatSessionService.createSession(newSession);
                });
        messages.add(new SendMessage(userId, "Соединение устанавливается.."));
        return messages;
    }


    /**
     * получаем список волонтеров
     */
    public List<SendMessage> openСonnection(List<SendMessage> messages) {
        Long idSessionForConnect = chatSessionService.getLastId(userId);
        if (!chatSessionService.checkSession(idSessionForConnect)) {
            chatSessionService.getChatSessionForClose(idSessionForConnect, SessionEnum.OPEN);
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
        chatSessionService.getChatSessionForClose(idSessionForConnect, SessionEnum.CLOSE);
        ChatSessionWithVolunteer chatUser = chatSessionService.getChatUser(idSessionForConnect);
        messages.add(new SendMessage(chatUser.getTelegramIdUser(), "Волонтер перевел Вас на бота, для повторной связи с волонтером нажмите кнопку Позвать волонтера"));
        return messages;
    }


    /**
     * метод получения всех волонтеров
     * */
    public List<User> getVolunteer() {
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
        getVolunteer().stream()
                .forEach(user -> {
                    messages.add(new SendMessage(user.getUserTelegramId(), "Просьба связаться с " + name + " по номеру " + phoneNumber));
                });
        return messages;
    }

}
