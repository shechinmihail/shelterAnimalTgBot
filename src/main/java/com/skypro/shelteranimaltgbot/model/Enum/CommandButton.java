package com.skypro.shelteranimaltgbot.model.Enum;
/**
 * Обработчики запросов
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
    SAFETY ( "Техника безопасности"),
    BACK("/back","Назад", generateCallbackData());

    private String commandText;
    private String description;
    private String callbackData;
    private static int count;

    CommandButton(String commandText) {
        this.commandText = commandText;
    }

    public String getCommandText() {
        return commandText;
    }

    CommandButton(String commandText, String description, String callbackData) {
        this.commandText = commandText;
        this.description = description;
        this.callbackData = callbackData;
    }

    public String getDescription() {
        return description;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public static String generateCallbackData() {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" ".repeat(Math.max(0, count + 1)));
        count += 1;
        return sb.toString();
    }

    @Override
    public String toString() {
        return "CommandButton{" +
                "commandText='" + commandText + '\'' +
                '}';
    }
}
