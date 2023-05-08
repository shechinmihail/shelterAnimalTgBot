package com.skypro.shelteranimaltgbot.service.Scheduled;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.model.Enum.ProbationPeriod;
import com.skypro.shelteranimaltgbot.model.Enum.ReportStatus;
import com.skypro.shelteranimaltgbot.model.Report;
import com.skypro.shelteranimaltgbot.service.AdoptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private final String notification = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private TelegramBot telegramBot;

    // @Scheduled(cron = "0 0/1 * * * *

    //@Scheduled(cron = "0 0 20 * * *")
    @Scheduled(cron = "0 0/1 * * * *")
    public void runReportCheck() {
        List<Adoption> adoptions = new ArrayList<>(getAdoptions());
        adoptions.stream()
                .forEach(adoption -> {
                    if (getPercentageRatio(adoption.getReports().size(), getBadReports(adoption).size()) > 30 && adoption.getReports().size() >= 7) {
                        telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), notification));
                    } else if (getPercentageRatio(adoption.getReports().size(), getBadReports(adoption).size()) > 50 && adoption.getReports().size() >= 14) {
                        setAdoptionTrialPeriod(adoption, 40);
                    } else if (getPercentageRatio(adoption.getReports().size(), getBadReports(adoption).size()) > 50 && adoption.getReports().size() >= 21) {
                        setAdoptionStatus(adoption, ProbationPeriod.NOT_PASSED);
                    } else if (getPercentageRatio(adoption.getReports().size(), getBadReports(adoption).size()) < 5 && adoption.getReports().size() >= 14) {
                        setAdoptionTrialPeriod(adoption, 20);
                    }
                });


    }

    private void checkLastDateReport(Adoption adoption) {

    }

    /**
     * метод изменения статуса записи усыновления
     */
    private void setAdoptionStatus(Adoption adoption, ProbationPeriod probationPeriod) {
        adoption.setProbationPeriod(probationPeriod);
        if (probationPeriod == ProbationPeriod.PASSED) {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "Поздравляем, Вы прошли испытательный срок, но вы можете продолжать радовать нас фотографиями наших любимых питомцев=)"));
        } else {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "К сожалению вы не прошли испытательный срок, в ближайшее время с Вами свяжется волонтер"));
        }
    }

    /**
     * метод изменяет количество дней испытательного срока и отправляет уведомление усыновителю
     */
    private void setAdoptionTrialPeriod(Adoption adoption, Integer days) {
        Integer daysNow = adoption.getTrialPeriod();
        adoption.setTrialPeriod(days);
        if (daysNow > adoption.getTrialPeriod()) {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "Испытательный срок будет сокращен до " + days + "дн."));
        } else {
            telegramBot.execute(new SendMessage(adoption.getUser().getUserTelegramId(), "Испытательный срок будет продлен до " + days + "дн."));
        }
    }

    /**
     * метод возвращает процент плохих отчетов
     */
    private double getPercentageRatio(Integer countAllReports, Integer countBadReports) {
        return countBadReports / countAllReports * 100;
    }

    /**
     * метод получения всех записей об усыновлении питомца которые на испытательном сроке
     */
    private List<Adoption> getAdoptions() {
        return new ArrayList<>((Collection) adoptionService.getAll().stream()
                .filter(adoption -> adoption.getProbationPeriod() == ProbationPeriod.PASSING));
    }

    /**
     * метод получения всех отчетов по записи усыновления
     */
    private List<Report> getReports(Adoption adoption) {
        return new ArrayList<>(adoption.getReports());
    }

    /**
     * метод получения плохих отчетов о питомце по записи усыновления
     */
    private List<Report> getBadReports(Adoption adoption) {
        return new ArrayList<>((Collection) adoption.getReports().stream()
                .filter(report -> report.getReportStatus() == ReportStatus.NOT_ACCEPTED));
    }

    /**
     * метод возвращает количество дней испытательного срока
     */
    private Integer getCountDaysTrialPeriod(Adoption adoption) {
        return adoption.getTrialPeriod();
    }

}
