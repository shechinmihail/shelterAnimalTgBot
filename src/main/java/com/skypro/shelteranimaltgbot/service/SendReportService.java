package com.skypro.shelteranimaltgbot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.skypro.shelteranimaltgbot.listener.TelegramBotUpdatesListener;
import com.skypro.shelteranimaltgbot.model.Enum.CommandButton;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import com.skypro.shelteranimaltgbot.repository.ReportRepository;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SendReportService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final Pattern REPORT_PATTERN = Pattern.compile(
            "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)");

    private final TelegramBot telegramBot;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public SendReportService(TelegramBot telegramBot, ReportService reportService,
                             ReportRepository reportRepository, UserRepository userRepository,
                             PetRepository petRepository) {
        this.telegramBot = telegramBot;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    public SendMessage reportMenu(Update update) {
        SendMessage report = new SendMessage(update.callbackQuery().message().chat().id(), "Отправить отчет");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(CommandButton.REPORT.getDescription())
                        .callbackData(CommandButton.REPORT.getCallbackData())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(CommandButton.CALL_VOLUNTEER.getDescription())
                        .callbackData(CommandButton.CALL_VOLUNTEER.getCallbackData())

        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(CommandButton.BACK.getDescription())
                        .callbackData(CommandButton.BACK.getCallbackData())
        );

        report.replyMarkup(inlineKeyboardMarkup);

        return report;
    }

    public SendMessage reportForm(Update update) {
        logger.info("Вызван метод отправляющий образец отчета для пользователя");
        SendMessage message = new SendMessage(update.callbackQuery().message().chat().id(),
                "ЗАГРУЗИТЕ ОТЧЕТ СОГЛАСНО ОБРАЗЦА: \n \n" +
                        "Рацион питания домашнего питомца \n" +
                        "Информация о общем самочувствии и привыкание к новому месту  \n" +
                        "Данные об изменении поведения и привычек домашнего питомца \n" +
                        "Фото домашнего питомца.");
        return message;
    }
    //TODO поправить все недочеты
    public void saveReport(Update update) {
        logger.info("Вызван метод сохранения отчета из чата телеграм");
        String text = update.message().caption();
        Matcher matcher = REPORT_PATTERN.matcher(text);
        Long chatId = update.message().chat().id();
        if (matcher.matches()) {
            String diet = matcher.group(3);
            String petInfo = matcher.group(6);
            String changeInPetBehavior = matcher.group(9);

            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();

                String photo = Arrays.toString(telegramBot.getFileContent(file));
                LocalDate date = LocalDate.now();
//                            if (userRepository.findById(Long.valueOf(id)) != null &&
//                        userRepository.findById(String.valueOf(chatId)).getStatus() != StatusEnum.ADOPTER) {
//                    reportService.saveReport(userTelegramId, diet, petInfo,
//                            changeInPetBehavior, photo, LocalDate.from(date.atStartOfDay()));
//                    telegramBot.execute(new SendMessage(chatId, "Отчет принят!"));
//                } else {
//                    telegramBot.execute(new SendMessage(chatId, "Вы еще не усыновили домашнего питомца!"));
//                }
            } catch (IOException e) {
                logger.error("Ошибка при загрузке фотографии");
                telegramBot.execute(new SendMessage(chatId,
                        "Ошибка при загрузке фотографии"));
            }
        } else {
            telegramBot.execute(new SendMessage(chatId,
                    "Отчет не заполнен полностью, проверьте все поля заполнения отчета и повторите отправку!"));
        }
    }

    //TODO сделать  @Scheduled
}
