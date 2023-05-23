package com.skypro.shelteranimaltgbot.service.scheduled;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.shelteranimaltgbot.model.ChatSessionWithVolunteer;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.model.enums.SessionEnum;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import com.skypro.shelteranimaltgbot.service.ButtonService;
import com.skypro.shelteranimaltgbot.service.ChatSessionWithVolunteerService;
import com.skypro.shelteranimaltgbot.service.CommandButtonService;
import com.skypro.shelteranimaltgbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledVolunteerChatSurvey {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private final ChatSessionWithVolunteerService chatSessionWithVolunteer;
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final CommandButtonService commandButtonService;
    private final ButtonService buttonService;
    private final UserRepository userRepository;

    public ScheduledVolunteerChatSurvey(ChatSessionWithVolunteerService chatSessionWithVolunteer, TelegramBot telegramBot, UserService userService, CommandButtonService commandButtonService, ButtonService buttonService,
                                        UserRepository userRepository) {
        this.chatSessionWithVolunteer = chatSessionWithVolunteer;
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.commandButtonService = commandButtonService;
        this.buttonService = buttonService;
        this.userRepository = userRepository;
    }

    /**
     * опрос чатов волонтеров для ожидающих юзеров
     */
    @Scheduled(cron = "0 0/1 * * * *")
    private void checkVolunteerChat() {
        List<ChatSessionWithVolunteer> sessionWithVolunteer = chatSessionWithVolunteer.getAllSession().stream()
                .filter(c -> c.getSession() == SessionEnum.STANDBY).toList();
        for (ChatSessionWithVolunteer c : sessionWithVolunteer) {
            User u = userRepository.findAByUserTelegramId(c.getTelegramIdUser());
            List<User> freeVolunteers = commandButtonService.getFreeVolunteer();
            if (freeVolunteers != null) {
                freeVolunteers.stream()
                        .forEach(user -> {
                            telegramBot.execute(new SendMessage(user.getUserTelegramId(), "нужна помощь " + " для " + u.getFirstName()).replyMarkup(buttonService.keyboardForChatSession()));
                            c.setTelegramIdVolunteer(user.getUserTelegramId());
                        });
            }
            chatSessionWithVolunteer.createSession(c);
        }
    }

    /**
     * закрытие всех открытых сессий с волонтером в 23-00 каждый день
     */
    @Scheduled(cron = "0 0 20 * * *")
    private void closeSessions() {
        List<ChatSessionWithVolunteer> chatSessionWithVolunteers = chatSessionWithVolunteer.getAllSession().stream()
                .filter(c -> c.getSession() == SessionEnum.OPEN).toList();
        for (ChatSessionWithVolunteer c : chatSessionWithVolunteers) {
            chatSessionWithVolunteer.getChatSessionForReplaceStatus(c.getId(), SessionEnum.CLOSE);
        }
    }


}
