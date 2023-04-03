package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.skypro.shelteranimaltgbot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Listener {

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramBot telegramBot;

    private final String START = "/start";
    private final String CALL_VOLUNTEER = "Позвать волонтера";


    /**
     * метод распределяющий входящие данные от клиента
     */
    public List<SendMessage> messages(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        var contact = update.message().contact();
        List<SendMessage> messages = new ArrayList<>();
        if (contact != null) {
            setContact(update);
            messages.add(new SendMessage(chatId, message.from().firstName() + " спасибо, мы свяжемся с вами в ближайшее время"));
        } else if (message.chat() == null) {
            messages.add(new SendMessage(chatId, "Ваш запрос обрабатывается"));
        } else {
            switch (message.text()) {
                case START:
                    userService.addUser(update);
                    messages.add(new SendMessage(chatId, "Привет " + message.from().firstName()).replyMarkup(keyboardMenu()));
                    messages.add(new SendMessage(chatId, "Выберете пункт меню:").replyMarkup(keyboardChatMenu()));
                    break;
                case CALL_VOLUNTEER:
                    chatJoinRequest(message, chatId);
                default:
                    break;
            }
        }

        return messages;
    }

    private void setContact(Update update) {
        userService.setContact(update);
    }


    /**
     * Функция позвать волонтера
     */
    public void chatJoinRequest(Message message, Long chatId) {
        List<User> volunteers = userService.cheUsersByRole();
        volunteers.stream()
                .forEach(u -> {
                            ForwardMessage forwardMessage = new ForwardMessage(u.getUserChatId(), chatId, message.messageId());
                            SendResponse response = telegramBot.execute(forwardMessage);
                        }
                );
    }


    /**
     * выпадающее меню в чат
     */
    private Keyboard keyboardChatMenu() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Узнать информацию о приюте").callbackData("Информация о приюте"),})
                .addRow(new InlineKeyboardButton[]{new InlineKeyboardButton("Как взять собаку из приюта").callbackData("Как взять собаку из приюта"),})
                .addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("Прислать отчет о питомце"))
                .addRow(new InlineKeyboardButton[]{new InlineKeyboardButton("Порекомендовать нас!").switchInlineQuery("Порекомендовать нас!")});
        return keyboard;
    }

    /**
     * нижнее меню
     */
    private Keyboard keyboardMenu() {
        Keyboard keyboard = new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton("Позвать волонтера"),
                        new KeyboardButton("Оставить контакт").requestContact(true),
                }
        ).resizeKeyboard(true);
        return keyboard;
    }


}