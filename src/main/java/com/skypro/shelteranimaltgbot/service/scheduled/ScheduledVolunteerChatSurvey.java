package com.skypro.shelteranimaltgbot.service.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class ScheduledVolunteerChatSurvey {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);


    @Scheduled
    public void checkVolunteerChat() {

    }

    public boolean triggerStart(boolean active) {
        log.info("Запущен опрос статуса чата волонтера");
        return active = true;
    }

    public boolean triggerFinish(boolean active) {
        log.info("Остановлен опрос статуса чата волонтера");
        return active = false;
    }

}
