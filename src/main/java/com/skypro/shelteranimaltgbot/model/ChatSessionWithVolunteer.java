package com.skypro.shelteranimaltgbot.model;

import com.skypro.shelteranimaltgbot.model.enums.SessionEnum;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "session")
public class ChatSessionWithVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long telegramIdVolunteer;
    private Long telegramIdUser;

    private SessionEnum session;

    public ChatSessionWithVolunteer() {
        this.session = SessionEnum.STANDBY;
    }

    public ChatSessionWithVolunteer(Long telegramIdVolunteer, Long telegramIdUser) {
        this.telegramIdVolunteer = telegramIdVolunteer;
        this.telegramIdUser = telegramIdUser;
        this.session = SessionEnum.STANDBY;
    }

    public Long getId() {
        return id;
    }

    public Long getTelegramIdVolunteer() {
        return telegramIdVolunteer;
    }

    public Long getTelegramIdUser() {
        return telegramIdUser;
    }


    public SessionEnum getSession() {
        return session;
    }

    public void setSession(SessionEnum session) {
        this.session = session;
    }

    public void setTelegramIdVolunteer(Long telegramIdVolunteer) {
        this.telegramIdVolunteer = telegramIdVolunteer;
    }

    public void setTelegramIdUser(Long telegramIdUser) {
        this.telegramIdUser = telegramIdUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatSessionWithVolunteer that = (ChatSessionWithVolunteer) o;
        return Objects.equals(id, that.id) && Objects.equals(telegramIdVolunteer, that.telegramIdVolunteer) && Objects.equals(telegramIdUser, that.telegramIdUser) && session == that.session;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegramIdVolunteer, telegramIdUser, session);
    }

    @Override
    public String toString() {
        return "ChatSessionWithVolunteer{" +
                "id=" + id +
                ", telegramIdVolunteer=" + telegramIdVolunteer +
                ", telegramIdUser=" + telegramIdUser +
                ", session=" + session +
                '}';
    }

}
