package com.skypro.shelteranimaltgbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    @Autowired
    private AdoptionService adoptionService;
    // @Scheduled(cron = "0 0/1 * * * *

    @Scheduled(cron = "0 0 20 * * *")
    public void run() {
//        List<Adoption> adoptions = new ArrayList<>((Collection) adoptionService.getAll().stream()
//                .filter(adoption -> adoption.getProbationPeriod() == ProbationPeriod.PASSING));
//        adoptions.stream()
//                .forEach(adoption -> {
//                    adoption.getReports()
//
//                        }
//                );
//        }

    }

}
