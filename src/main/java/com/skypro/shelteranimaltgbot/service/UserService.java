package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.Update;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Role;
import com.skypro.shelteranimaltgbot.model.Status;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.RoleRepository;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    RoleService roleService;


    public void addUser(Update update) {
        var message = update.message().from();
        var chatId = update.message().chat().id();
        if (userRepository.findByUserId(message.id()) == 0) {
            Status status = new Status("Гость");
            User user = new User(message.firstName(), message.lastName(), message.id(), chatId, null, roleRepository.save(new Role(RoleEnum.VOLUNTEER)));
            userRepository.save(user);
        }

    }

    public void setContact(Update update) {
        var phone = update.message().contact().phoneNumber();
        var userTelegramId = update.message().from().id();
        User u = userRepository.findByUserTelegramId(userTelegramId);
        u.setPhone(phone);
        userRepository.save(u);
    }


    public List<User> cheUsersByRole() {
        // return new ArrayList<User> (userRepository.findByRole(roleService.addRole(new Role(RoleEnum.VOLUNTEER)));
        List<User> userList = userRepository.findAll();

        return userList.stream().filter(user -> user.getRole().getRole().equals(RoleEnum.VOLUNTEER)).toList();
    }
}