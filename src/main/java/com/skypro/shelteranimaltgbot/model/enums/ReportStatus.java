package com.skypro.shelteranimaltgbot.model.enums;

/**
 * Enum ReportStatus
 * Используется для обозначения статуса отчетов
 */
public enum ReportStatus {

    POSTED("Отправлен"),
    ACCEPTED("Принят"),
    NOT_ACCEPTED("Не принят");

    /**
     * Поле "Описание"
     */
    private final String description;

    /**
     * Конструктор создания статуса отчета
     *
     * @param description описание статуса
     */
    ReportStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
