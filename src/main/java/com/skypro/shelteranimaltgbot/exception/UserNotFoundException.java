package com.skypro.shelteranimaltgbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение UserNotFoundException
 * Срабатывает, когда в БД не найден пользователь
 * Наследование от {@link RuntimeException}
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    /**
     * Конструктор исключения
     * Показ сообщения, что пользователь небыл найден в БД
     */
    public UserNotFoundException() {
        super("Такого пользователя нет в БД");
    }
}
