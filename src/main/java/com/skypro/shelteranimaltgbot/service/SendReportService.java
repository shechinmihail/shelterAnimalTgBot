package com.skypro.shelteranimaltgbot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.skypro.shelteranimaltgbot.listener.TelegramBotUpdatesListener;
import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.model.Report;
import com.skypro.shelteranimaltgbot.model.enums.ProbationPeriod;
import com.skypro.shelteranimaltgbot.model.enums.ReportStatus;
import com.skypro.shelteranimaltgbot.model.enums.RoleEnum;
import com.skypro.shelteranimaltgbot.model.enums.StatusEnum;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import com.skypro.shelteranimaltgbot.repository.ReportRepository;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SendReportService {

    private static final Pattern REPORT_PATTERN = Pattern.compile(
            "([А-яA-z\\s\\d\\D]+):(\\s)([0-9\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)\n" +
                    "([А-яA-z\\s\\d\\D]+):(\\s)([А-яA-z\\s\\d\\D]+)");

    private final String TEXT_TEMPLATE = "<b>ИНСТРУКЦЯИЯ ЗАПОЛНЕНИЯ ОТЧЕТА:</b> \n\n" +
            "1) <i>Скопируйте текст шаблона ниже</i> \n" +
            "2) <i>Сфотографируйте питомца</i> \n" +
            "3) <i>Вставьте скопированный шаблон в описание к фото</i> \n" +
            "4) <i>Замените ХХХ своими комментариями</i>";

    private final String TEMPLATE = "Id: ХХХ \n" +
            "Рацион: XXX \n" +
            "Самочувствие: XXX \n" +
            "Поведение: XXX \n";

    @Value("${path.to.avatars.from.report.folder}")
    private String reportPhotoDir;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final UserService userService;
    private final AdoptionService adoptionService;
    private final ButtonService buttonService;


    public SendReportService(TelegramBot telegramBot, ReportService reportService,
                             ReportRepository reportRepository, UserRepository userRepository,
                             PetRepository petRepository, UserService userService, AdoptionService adoptionService, ButtonService buttonService) {
        this.telegramBot = telegramBot;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.userService = userService;
        this.adoptionService = adoptionService;
        this.buttonService = buttonService;
    }

    public List<SendMessage> reportForm(Long id, List<SendMessage> messages) {
        logger.info("Вызван метод отправляющий образец отчета для пользователя");
        messages.add(new SendMessage(id, TEXT_TEMPLATE).parseMode(ParseMode.HTML));
        messages.add(new SendMessage(id, TEMPLATE));
        return messages;
    }

    public void saveReport(Update update) {
        logger.info("Вызван метод сохранения отчета из чата телеграм");
        String text = update.message().caption();
        Matcher matcher = REPORT_PATTERN.matcher(text);
        Long chatId = update.message().chat().id();
        if (matcher.matches()) {
            Long petId = Long.valueOf(matcher.group(3).replaceAll(" ", ""));
            String diet = matcher.group(6);
            String petInfo = matcher.group(9);
            String changeInPetBehavior = matcher.group(12);
            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();
                String photo = Arrays.toString(telegramBot.getFileContent(file));
                if (userRepository.findAByUserTelegramId(update.message().from().id()) != null &&
                        userService.checkUserStatus(chatId) == StatusEnum.ADOPTER) {
                    Adoption adoption = adoptionService.findAdoptionByPet(petId, update, ProbationPeriod.PASSING);
                    if (adoption == null) {
                        telegramBot.execute(new SendMessage(chatId, "Неверно указан номер питомца"));
                    } else {
                        Report report = new Report();
                        report.setReportStatus(ReportStatus.POSTED);
                        report.setDiet(diet);
                        report.setPhoto(photo);
                        report.setChangeInPetBehavior(changeInPetBehavior);
                        report.setPetInfo(petInfo);
                        report.setUserTelegramId(update.message().from().id());
                        report.setAdoption(adoption);
                        reportRepository.save(report);
                        telegramBot.execute(new SendMessage(chatId, "Отчет принят на рассмотрение!"));
                        userService.checkUsersByRole(RoleEnum.VOLUNTEER).stream()
                                .forEach(user -> {
                                    ForwardMessage forwardMessage = new ForwardMessage(user.getUserTelegramId(), chatId, update.message().messageId());
                                    SendResponse response = telegramBot.execute(forwardMessage);
                                    telegramBot.execute(new SendMessage(user.getUserTelegramId(),
                                            "<b>ПРИШЕЛ НОЫЙ ОТЧЕТ № " +
                                                    report.getId() + " " + report.getDate() + "</b> \n"
                                    ).replyMarkup(buttonService.buttonForVolunteerOfReport(update, report)).parseMode(ParseMode.HTML));
                                });
                    }

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
}
