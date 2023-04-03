package com.skypro.shelteranimaltgbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import com.skypro.shelteranimaltgbot.service.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private Listener listener;


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing updatePhone: {}", update);
            listener.messages(update).stream()
                    .forEach(sendMessage -> {
                        if (update.message() != null) {
                            SendResponse response = telegramBot.execute(sendMessage);
                        }
                    });
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
