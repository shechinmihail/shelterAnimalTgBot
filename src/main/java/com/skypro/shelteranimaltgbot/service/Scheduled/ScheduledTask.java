package com.skypro.shelteranimaltgbot.service.Scheduled;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.model.Enum.ProbationPeriod;
import com.skypro.shelteranimaltgbot.model.Enum.ReportStatus;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;
import com.skypro.shelteranimaltgbot.model.Report;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.AdoptionRepository;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import com.skypro.shelteranimaltgbot.service.AdoptionService;
import com.skypro.shelteranimaltgbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private final String notification = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private TelegramBot telegramBot;

    private final double PERCENT1 = 30.00;
    private final double PERCENT2 = 50.00;
    private final double PERCENT3 = 5.00;
    @Autowired
    private UserService userService;
    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private UserRepository userRepository;

    //@Scheduled(cron = "0 0 20 * * *")
    @Scheduled(cron = "0 0/1 * * * *")
    public void runReportCheck() {
        List<Adoption> adoptions = new ArrayList<>(getAdoptions());
        adoptions.stream()
                .forEach(adoption -> {
                    checkLastDateReport(adoption);
                    double percent = getPercentageRatio(adoption.getReports().size(), getBadReports(adoption).size());
                    if (percent > PERCENT2 && adoption.getReports().size() >= 21) {
                        log.info("> 50 проц после замены срок 21 дн");
                        setAdoptionStatus(adoption, ProbationPeriod.NOT_PASSED);
                    } else if (percent > PERCENT2 && adoption.getReports().size() >= 14) {
                        log.info("> 50 проц + замена 40 дн");
                        setAdoptionTrialPeriod(adoption, 40);
                    } else if (percent > PERCENT1 && adoption.getReports().size() >= 7) {
                        log.info("> 30 проц");
                        telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), notification));
                    } else if (percent < PERCENT3 && adoption.getReports().size() >= 14) {
                        log.info("< 5 проц замена до 20 дн");
                        setAdoptionTrialPeriod(adoption, 20);
                        setAdoptionStatus(adoption, ProbationPeriod.PASSED);
                    }
                });
    }

    /**
     * метод определяет просроченные отчеты
     */

    private void checkLastDateReport(Adoption adoption) {
        log.info("Вызван метод выявления просроченных отчетов");
        List<Report> reports = new ArrayList<>(adoption.getReports());
        try {
            Report lastReport = reports.stream().max(Comparator.comparing(Report::getDate)).orElse(null);
            if (lastReport.getDate().isBefore(LocalDate.now().minusDays(1))) {
                List<User> volunteers = userService.checkUsersByRole(RoleEnum.VOLUNTEER);
                for (User volunteer : volunteers) {
                    String textToVolunteer = "Отчёт о животном: (ID=" + adoption.getPet().getId() + ") " + adoption.getPet().getName() +
                            " от усыновителя: (ID =" + adoption.getUser().getId() + ") " + adoption.getUser().getFirstName() +
                            " не поступал больше 2-х дней." + "\n" +
                            "Дата последнего отчета: " + lastReport.getDate();
                    telegramBot.execute(new SendMessage(volunteer.getUserTelegramId(), textToVolunteer));
                }
            }

            if (lastReport.getDate().equals(LocalDate.now().minusDays(1))) {
                String textToUser = "Уважаемый " + adoption.getUser().getFirstName() + " не забудьте сегодня отправить отчет";
                SendMessage messageToUser = new SendMessage(lastReport.getUserTelegramId(), textToUser);
                telegramBot.execute(messageToUser);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }


    }

    /**
     * метод изменения статуса записи усыновления
     */
    private void setAdoptionStatus(Adoption adoption, ProbationPeriod probationPeriod) {
        log.info("Вызван метод изменения статуса записи усыновления");
        adoption.setProbationPeriod(probationPeriod);
        adoptionRepository.save(adoption);
        if (probationPeriod == ProbationPeriod.PASSED) {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "Поздравляем, Вы прошли испытательный срок, но вы можете продолжать радовать нас фотографиями наших любимых питомцев=)"));
        } else {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "К сожалению вы не прошли испытательный срок, в ближайшее время с Вами свяжется волонтер"));
            getHandlingBadReports(adoption);
        }
    }

    /**
     * метод отправляет уведомление волонтерам о том что юзер не прошел испытательный срок, а также изменяет статус юзера
     */
    private void getHandlingBadReports(Adoption adoption) {
        List<User> volunteers = userService.checkUsersByRole(RoleEnum.VOLUNTEER);
        volunteers.stream().forEach(user -> {
            telegramBot.execute(new SendMessage(user.getUserTelegramId(), "Опекун (ID=" + adoption.getUser().getId() + ") " + adoption.getUser().getFirstName() + " не прошел испытательный срок, просьба связаться с ним! "));
        });
        adoption.getUser().setStatus(StatusEnum.BAD_USER);
        userRepository.save(adoption.getUser());
    }

    /**
     * метод изменяет количество дней испытательного срока и отправляет уведомление усыновителю
     */
    private void setAdoptionTrialPeriod(Adoption adoption, Integer days) {
        log.info("Вызван метод изменения количества дней испытательного срока");
        Integer daysNow = adoption.getTrialPeriod();
        adoption.setTrialPeriod(days);
        adoptionRepository.save(adoption);
        if (daysNow > adoption.getTrialPeriod()) {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "Испытательный срок будет сокращен до " + days + "дн."));
        } else if (daysNow < adoption.getTrialPeriod()) {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "Испытательный срок будет продлен до " + days + "дн."));
        }
    }

    /**
     * метод возвращает процент плохих отчетов
     */
    private double getPercentageRatio(Integer countAllReports, Integer countBadReports) {
        log.info("Вызван метод возвращения количества плохих отчетов");
        return countBadReports * 100 / countAllReports;
    }

    /**
     * метод получения всех записей об усыновлении питомца которые на испытательном сроке
     */
    private List<Adoption> getAdoptions() {
        log.info("Вызван метод получения всех записей усыновлений которые на испытательном сроке");
        return new ArrayList<>(adoptionService.getAll().stream()
                .filter(adoption -> adoption.getProbationPeriod() == ProbationPeriod.PASSING).toList());
    }

    /**
     * метод получения всех отчетов по записи усыновления
     */
    private List<Report> getReports(Adoption adoption) {
        log.info("Вызван метод получения всех отчетов по записи усыновления");
        return new ArrayList<>(adoption.getReports());
    }

    /**
     * метод получения плохих отчетов о питомце по записи усыновления
     */
    private List<Report> getBadReports(Adoption adoption) {
        log.info("Вызван метод получения плохих отчетов о питомце по записи усыновления");
        return new ArrayList<>(adoption.getReports().stream()
                .filter(report -> report.getReportStatus() == ReportStatus.NOT_ACCEPTED).toList());
    }

    /**
     * метод возвращает количество дней испытательного срока
     */
    private Integer getCountDaysTrialPeriod(Adoption adoption) {
        log.info("Вызван метод получения количества дней испытательного срока");
        return adoption.getTrialPeriod();
    }

}
