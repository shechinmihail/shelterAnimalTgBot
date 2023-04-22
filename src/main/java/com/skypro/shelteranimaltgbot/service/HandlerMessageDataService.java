package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HandlerMessageDataService {

    /**
     * команды
     */
    private final String START = "/start";
    private final String CALL_VOLUNTEER = "Позвать волонтера";
    private final String OPEN = "Принять";
    private final String CLOSE = "Закрыть/Отклонить";

    @Autowired
    private CommandButtonService commandButtonService;
    @Autowired
    UserService userService;
    @Autowired
    TelegramBot telegramBot;
    @Autowired
    private ChatSessionWithVolunteerService chatSessionService;


    //TODO убрать message userId
    private Message message;
    private Long userId;

    public List<SendMessage> handlerMessageData(Update update, List<SendMessage> messages) {
        message = update.message();
        userId = message.from().id();
        String text = update.message().text();
        var contact = update.message().contact();

        if (contact != null) {
            commandButtonService.setContact(update);
            messages.add(new SendMessage(userId, message.from().firstName() + " спасибо, мы свяжемся с вами в ближайшее время"));
            commandButtonService.sendNotification(update, messages);

        } else {
            switch (text) {
                case START -> commandButtonService.mainMenu(update, messages);
                case CALL_VOLUNTEER -> commandButtonService.callVolunteer(update, messages);
                case OPEN -> commandButtonService.openСonnection(messages);
                case CLOSE -> commandButtonService.closeСonnection(update, messages);
                default -> chatWithVolunteer(userId);
            }
        }
        return messages;

    }

    private void chatWithVolunteer(Long userId) {
        Long idSessionForConnect = chatSessionService.getLastId(userId);
        Long userTelegramId = chatSessionService.getChatUser(idSessionForConnect).getTelegramIdUser();
        Long volunteerChatId = chatSessionService.getChatUser(idSessionForConnect).getTelegramIdVolunteer();
        if (chatSessionService.checkSession(idSessionForConnect) && !userId.equals(userTelegramId)) {
            ForwardMessage forwardMessage = new ForwardMessage(userTelegramId, userId, message.messageId());
            SendResponse response = telegramBot.execute(forwardMessage);
        } else if (chatSessionService.checkSession(idSessionForConnect) && !userId.equals(volunteerChatId)) {
            ForwardMessage forwardMessage = new ForwardMessage(volunteerChatId, userId, message.messageId());
            SendResponse response = telegramBot.execute(forwardMessage);
        }
    }











}
