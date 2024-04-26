package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс UserRepository
 * для работы с БД (для работы с ролями пользователей)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет всех юзеров по id
     **/
    User findAByUserTelegramId(Long id);


    /**
     * Метод возвращает пользователя по роли (User / Volunteer)
     */

    List<User> findAllByRole(RoleEnum role);



}