package com.skypro.shelteranimaltgbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import com.skypro.shelteranimaltgbot.service.ForwardListenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Класс, обрабатывающий все апдейты для бота.
 * Содержит вспомогательный класс ForwardListenerService,
 * распределяющий входящие данные от клиента.
 */

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final ForwardListenerService forwardListenerService;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, ForwardListenerService forwardListenerService) {
        this.telegramBot = telegramBot;
        this.forwardListenerService = forwardListenerService;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing updatePhone: {}", update);
              forwardListenerService.messages(update).stream()
                    .forEach(sendMessage -> {
                        SendResponse response = telegramBot.execute(sendMessage); // залогировать отправилось сообщение или нет
                        if (response.isOk()) {
                            logger.info("Message sent");
                        } else {
                            logger.error("Message was not sent because of " + response.errorCode());
                        }

                    });
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
