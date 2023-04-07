package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.Update;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public void addUser(Update update) {
        var message = update.message().from();
        var chatId = update.message().chat().id();
        User user = new User(message.firstName(), message.lastName(), message.id(), chatId, StatusEnum.GUEST, RoleEnum.USER);
        if (userRepository.findAllByUserTelegramId(message.id()) == null) {
            userRepository.save(user);
        }

    }

    public void setContact(Update update) {
        var phone = update.message().contact().phoneNumber();
        var userTelegramId = update.message().from().id();
        User u = userRepository.findAllByUserTelegramId(userTelegramId);
        u.setPhone(phone);
        userRepository.save(u);
    }


    public List<User> cheUsersByRole(RoleEnum role) {
        return new ArrayList<>(userRepository.findAllByRole(role));
    }

    public void setChatIdForConnect(Long chatIdForConnect, Long telegramId) {
        User user = userRepository.findAllByUserTelegramId(telegramId);
        user.setUserChatId(chatIdForConnect);
        userRepository.save(user);
    }
}