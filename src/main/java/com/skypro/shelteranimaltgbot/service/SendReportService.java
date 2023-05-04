package com.skypro.shelteranimaltgbot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.skypro.shelteranimaltgbot.listener.TelegramBotUpdatesListener;
import com.skypro.shelteranimaltgbot.model.Enum.ReportStatus;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import com.skypro.shelteranimaltgbot.model.Report;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import com.skypro.shelteranimaltgbot.repository.ReportRepository;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SendReportService {

    private static final Pattern REPORT_PATTERN = Pattern.compile(
            "([А-яA-z\\s\\d\\D]+):[0-9]" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)");
    @Value("${path.to.avatars.from.report.folder}")
    private String reportPhotoDir;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
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

    public SendMessage reportForm(Long id) {
        logger.info("Вызван метод отправляющий образец отчета для пользователя");
        SendMessage message = new SendMessage(id,
                "ШАБЛОН ЗАПОЛНЕНИЯ ОТЧЕТА: \n \n" +
                        "Фото домашнего питомца. \n" +
                        "Id питомца: ХХХ \n" +
                        "Рацион: XXXX XXXX XXXX \n" +
                        "Самочувствие: XXXX XXXX XXXX \n" +
                        "Поведение: XXXX XXXX XXXX \n"
        );
        return message;
    }

    //TODO поправить все недочеты изменить метод сохранения фотографии в локальную папку с сохранением ссылки на нее в БД
    //TODO добавить метод проверки принадлежности питомца опекуну
    public void saveReport(Update update) {
        logger.info("Вызван метод сохранения отчета из чата телеграм");
        String text = update.message().caption();
        Matcher matcher = REPORT_PATTERN.matcher(text);
        Long chatId = update.message().chat().id();
        if (matcher.matches()) {
            Long petId = Long.valueOf(matcher.group(3));
            String diet = matcher.group(6);
            String petInfo = matcher.group(9);
            String changeInPetBehavior = matcher.group(12);

            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();

                String photo = Arrays.toString(telegramBot.getFileContent(file));


                if (userRepository.findAllByUserTelegramId(update.message().from().id()) != null &&
                        userRepository.findAllByUserTelegramId(update.message().from().id()).getStatus() != StatusEnum.ADOPTER) {

                    Report report = new Report();
                    report.setReportStatus(ReportStatus.POSTED);
                    report.setDiet(diet);
                    report.setPhoto(photo);
                    report.setChangeInPetBehavior(changeInPetBehavior);
                    report.setPetInfo(petInfo);
                    report.setUserTelegramId(update.message().from().id());
                    reportRepository.save(report);
                    telegramBot.execute(new SendMessage(chatId, "Отчет принят!"));
                } else {
                    telegramBot.execute(new SendMessage(chatId, "Вы еще не усыновили домашнего питомца!"));

                }
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
