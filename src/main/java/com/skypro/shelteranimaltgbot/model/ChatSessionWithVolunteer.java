package com.skypro.shelteranimaltgbot.model;

import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "session")
public class ChatSessionWithVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long telegramIdVolunteer;
    private Long getTelegramIdUser;
    private Long chatIdUser;
    private SessionEnum session;

    public ChatSessionWithVolunteer() {
    }

    public ChatSessionWithVolunteer(Long telegramIdVolunteer, Long getTelegramIdUser, Long chatIdUser, SessionEnum session) {
        this.telegramIdVolunteer = telegramIdVolunteer;
        this.getTelegramIdUser = getTelegramIdUser;
        this.chatIdUser = chatIdUser;
        this.session = session;
    }

    public Long getId() {
        return id;
    }

    public Long getTelegramIdVolunteer() {
        return telegramIdVolunteer;
    }

    public Long getGetTelegramIdUser() {
        return getTelegramIdUser;
    }

    public Long getChatIdUser() {
        return chatIdUser;
    }

    public SessionEnum getSession() {
        return session;
    }

    public void setSession(SessionEnum session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatSessionWithVolunteer that = (ChatSessionWithVolunteer) o;
        return Objects.equals(id, that.id) && Objects.equals(telegramIdVolunteer, that.telegramIdVolunteer) && Objects.equals(getTelegramIdUser, that.getTelegramIdUser) && Objects.equals(chatIdUser, that.chatIdUser) && session == that.session;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegramIdVolunteer, getTelegramIdUser, chatIdUser, session);
    }

    @Override
    public String toString() {
        return "ChatSessionWithVolunteer{" +
                "id=" + id +
                ", telegramIdVolunteer=" + telegramIdVolunteer +
                ", getTelegramIdUser=" + getTelegramIdUser +
                ", chatIdUser=" + chatIdUser +
                ", session=" + session +
                '}';
    }
}
