package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class HandlerMessageDataService {

    private final String START = "/start";
    private final String CALL_VOLUNTEER = "Позвать волонтера";
    private final String OPEN = "Принять";
    private final String CLOSE = "Закрыть/Отклонить";

    @Autowired
    private  CommandButtonService commandButtonService;


    //TODO убрать message userId
    private Message message;
    private Long userId;

    public List<SendMessage> handlerMessageData(Update update, List<SendMessage> messages) {

        String text = message.text();
        var contact = message.contact();

        if (contact != null) {
            commandButtonService.setContact(update);
            messages.add(new SendMessage(userId, message.from().firstName() + " спасибо, мы свяжемся с вами в ближайшее время"));
            commandButtonService.sendNotification(update, messages);

        } else {
            switch (text) {
                case START -> commandButtonService.mainMenu(update, messages);
                case CALL_VOLUNTEER -> commandButtonService.callVolunteer(update, messages);
                case OPEN ->commandButtonService.openСonnection(messages);
                case CLOSE -> commandButtonService.closeСonnection(messages);
                default -> chatWithVolunteer(userId);
            }
        }
        return messages;

    }
    private void chatWithVolunteer(Long userId) {

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











}
