package com.skypro.shelteranimaltgbot.model.Enum;
/**
 * обработчики запросов
 */
public enum CommandButton {

    START ( "/start"),
    LEAVE_CONTACT ("Оставить контакт"),
    ABOUT ( "О приюте"),
    OPEN ( "Принять"),
    TAKE_PET ( "Как взять питомца из приюта"),
    REPORT ( "Прислать отчет о питомце"),
    SHARE ( "Рекомендация.."),
    CALL_VOLUNTEER ( "Позвать волонтера"),
    CLOSE ( "Закрыть/Отклонить"),
    ABOUT_SHELTER ( "О приюте подробнее"),
    OPERATING_MODE ( "Режим работы/Адрес"),
    VIEW_ALL_ANIMALS ( "Посмотреть список животных"),
    SAFETY ( "Техника безопасности");

    private String commandText;

    CommandButton(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }

    @Override
    public String toString() {
        return "CommandButton{" +
                "commandText='" + commandText + '\'' +
                '}';
    }
}
