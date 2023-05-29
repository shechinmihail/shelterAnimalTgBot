package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет всех юзеров по id
     **/
    User findAByUserTelegramId(Long id);


    /**
     * Метод возвращает сотрудников по роли (User / Volunteer)
     */

    List<User> findAllByRole(RoleEnum role);



}