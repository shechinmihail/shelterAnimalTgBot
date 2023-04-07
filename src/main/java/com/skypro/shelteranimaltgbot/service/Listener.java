package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
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
        } else {
            switch (message.text()) {
                case START:
                    userService.addUser(update);
                    messages.add(new SendMessage(chatId, "Привет " + message.from().firstName()).replyMarkup(keyboardMenu()));
                    messages.add(new SendMessage(chatId, "Выберете пункт меню:").replyMarkup(keyboardChatMenu()));
                    break;
                case CALL_VOLUNTEER:
                    chatJoinRequest().stream()
                            .forEach(user -> {
                                messages.add(new SendMessage(user.getUserChatId(), "нужна помощь " + " для " + message.from().firstName()));
                                messages.add(new SendMessage(user.getUserChatId(), "принять запрос").replyMarkup(keyboardSession()));
                            });
                    messages.add(new SendMessage(chatId, "Соединение устанавливается.."));
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
     * Метод позвать волонтера
     */
//    public void chatJoinRequest(Message message, Long chatId) {
//        List<User> volunteers = userService.cheUsersByRole();
//        volunteers.stream()
//                .forEach(u -> {
//                            ForwardMessage forwardMessage = new ForwardMessage(u.getUserChatId(), chatId, message.messageId());
//                            SendResponse response = telegramBot.execute(forwardMessage);
//                        }
//                );
//    }

    /**
     * получаем список волонтеров
     */
    public List<User> chatJoinRequest() {
        List<User> volunteers = userService.cheUsersByRole();
        return new ArrayList<>(userService.cheUsersByRole());
    }


    /**
     * выпадающее меню в чат
     */
    private Keyboard keyboardChatMenu() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("Узнать информацию о приюте").callbackData("ABOUT"))
                .addRow(new InlineKeyboardButton("Как взять собаку из приюта").callbackData("Как взять собаку из приюта"))
                .addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("Прислать отчет о питомце"))
                .addRow(new InlineKeyboardButton("Порекомендовать нас!").switchInlineQuery("Порекомендовать нас!"));
    }

    /**
     * нижнее меню
     */
    private Keyboard keyboardMenu() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton("Позвать волонтера"),
                new KeyboardButton("Оставить контакт").requestContact(true)).resizeKeyboard(true);
    }


}