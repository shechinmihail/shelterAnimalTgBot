package com.skypro.shelteranimaltgbot.model.enums;


/**
 * Класс Enum StatusPet
 * Используется для статуса домашнего питомца
 */
public enum StatusPet {
    FREE("Без хозяина"),
    BUSY("С хозяином");

    /**
     * Поле: Описание
     */
    private final String description;

    /**
     * Конструктор - создания статуса
     *
     * @param description описание статуса домашнего питомца
     */
    StatusPet(String description) {
        this.description = description;
    }

    /**
     * Получение статуса домашнего питомца
     *
     * @return статус домашнего питомца
     */
    public String getDescription() {
        return description;
    }
}
