package com.skypro.shelteranimaltgbot.model.Enum;

/**
 * обработчики запросов
 */
public enum CommandButton {

    START("/start"),
    LEAVE_CONTACT("Оставить контакт"),
    ABOUT("О приюте"),
    OPEN("Принять"),
    TAKE_PET("Как взять питомца из приюта"),
    REPORT("Прислать отчет о питомце"),
    SHARE("Рекомендация.."),
    CALL_VOLUNTEER("Позвать волонтера"),
    CLOSE("Закрыть/Отклонить"),
    ABOUT_SHELTER("О приюте подробнее"),
    OPERATING_MODE("Режим работы/Адрес"),
    VIEW_ALL_ANIMALS("Посмотреть список животных"),
    SAFETY("Техника безопасности");
//    RULES_OF_ACQUAINTANCE("Правила знакомства"),
//    DOCUMENTS("Список документов для усыновления"),
//    TRANSPORT_RECOMMENDATIONS ("Рекомендации по транспортировке"),
//    RECOMMENDATIONS_FOR_KID_HOUSE ("Обустройство дома для щенка/котенка"),
//    RECOMMENDATIONS_FOR_ADULT_HOUSE ("Обустройство дома для взрослого животного"),
//    RECOMMENDATIONS_FOR_INVALID_HOUSE ("Обустройство дома для животного с ограниченными возможностями"),
//    REASONS_FOR_REJECTION ("Причины отказа в усыновлении животного"),
//    CYNOLOGIST_ADVICE ("Советы кинолога"),
//    BEST_CYNOLOGISTS ("Проверенные кинологи"),
//    TAKE_CAT ("Взять кошку"),
//    TAKE_DOG ("Взять собаку");

    private final String commandText;

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
