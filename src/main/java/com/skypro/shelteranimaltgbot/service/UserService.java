package com.skypro.shelteranimaltgbot.service;

import com.pengrad.telegrambot.model.Update;
import com.skypro.shelteranimaltgbot.exception.UserNotFoundException;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Сервис UserService
 * Сервис для добавления, удаления, редактирования и поиска пользователей в базе данных
 */
@Service
public class UserService {

    /**
     * Поле репозитория пользователя
     */
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    /**
     * Добавление нового пользователя и сохранение его в базе данных
     *
     * @param user
     */
    public User addUser(User user) {
        logger.info("Вызван метод добавления пользователя");
        if (userRepository.findAllByUserTelegramId(user.getUserTelegramId()) == null) {
            userRepository.save(user);
            logger.info("Пользователь добавлен {}", user.getFirstName() + " " + user.getLastName());
        }
        logger.info("Пользователь {}, существует", user.getFirstName() + " " + user.getLastName());
        return user;
    }


    /**
     * Поиск пользователя в базе данных по идентификатору (id)
     *
     * @param id идентификатор пользователя, не может быть null
     * @return найденый пользователь
     * @throws EntityNotFoundException если пользователь с указаным id не был найден в базе данных
     */
    public User findUser(Long id) {
        logger.info("Вызван метод поиска пользователя по идентификатору (id)");
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    /**
     * Изменение данных пользователя в базе данных
     *
     * @param user редактируемый пользователь
     * @return отредактированный пользователь
     * @throws UserNotFoundException если пользователь не найден
     */
    public User updateUser(User user) {
        logger.info("Вызван метод редактирования пользователя в базе данных");
        if (user.getId() != null) {
            if (findUser(user.getId()) != null) {
                return userRepository.save(user);
            }
        }
        logger.error("По запросу пользователь не найден");
        throw new UserNotFoundException();
    }


    /**
     * Получение списка пользователей из базы данных
     *
     * @return список(коллекцию) пользователей
     */
    public Collection<User> getAll() {
        logger.info("Вызван метод получения всех пользователей");
        return userRepository.findAll();
    }


    /**
     * Удаление пользователя из базы данных по идентификатору (id)
     *
     * @param id пользователя, не может быть null
     */
    public void deleteUser(Long id) {
        logger.info("Вызван метод удаления пользователя по идентификатору (id)");
        userRepository.deleteById(id);
    }


    /**
     * метод добавления контакта в БД
     * */
    public void setContact(Update update) {
        logger.info("Вызван метод добавлкения номера телефона в БД");
        var phone = update.message().contact().phoneNumber();
        var userTelegramId = update.message().from().id();
        User u = userRepository.findAllByUserTelegramId(userTelegramId);
        u.setPhone(phone);
        userRepository.save(u);
    }

    /**
     * поиск юзера по айди
     */
    public User findUserByChatId(Update update) {
        User findUser = userRepository.findAllByUserTelegramId(update.message().chat().id());
        return findUser;
    }


    /**
     * вернуть всех юзеров по определенной роли
     * */
    public List<User> checkUsersByRole(RoleEnum role) {
        logger.info("Вызван метод получения всех пользователе с ролью {}", role);
        return new ArrayList<>(userRepository.findAllByRole(role));
    }

}