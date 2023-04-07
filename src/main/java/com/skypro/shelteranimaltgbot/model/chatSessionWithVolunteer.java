package com.skypro.shelteranimaltgbot.model;

import com.skypro.shelteranimaltgbot.model.Enum.SessionEnum;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "session")
public class chatSessionWithVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatIdVolunteer;
    private Long chatIdUser;
    private SessionEnum session;

    public chatSessionWithVolunteer() {
    }

    public chatSessionWithVolunteer(Long chatIdVolunteer, Long chatIdUser, SessionEnum session) {
        this.chatIdVolunteer = chatIdVolunteer;
        this.chatIdUser = chatIdUser;
        this.session = session;
    }

    public Long getId() {
        return id;
    }

    public Long getChatIdVolunteer() {
        return chatIdVolunteer;
    }

    public Long getChatIdUser() {
        return chatIdUser;
    }

    public SessionEnum getSession() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        chatSessionWithVolunteer that = (chatSessionWithVolunteer) o;
        return Objects.equals(id, that.id) && Objects.equals(chatIdVolunteer, that.chatIdVolunteer) && Objects.equals(chatIdUser, that.chatIdUser) && session == that.session;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatIdVolunteer, chatIdUser, session);
    }

    @Override
    public String toString() {
        return "chatSessionWithVolonter{" +
                "id=" + id +
                ", chatIdVolunteer=" + chatIdVolunteer +
                ", chatIdUser=" + chatIdUser +
                ", session=" + session +
                '}';
    }
}
