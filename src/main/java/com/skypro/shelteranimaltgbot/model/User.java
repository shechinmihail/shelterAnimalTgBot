package com.skypro.shelteranimaltgbot.model;

import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Long userTelegramId;
    private Long userChatId;

    private StatusEnum status;
    private String phone;

    private RoleEnum role;

    public User() {
    }

    public User(String firstName, String lastName, Long userTelegramId, Long userChatId, StatusEnum status, RoleEnum role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userTelegramId = userTelegramId;
        this.userChatId = userChatId;
        this.status = status;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getUserTelegramId() {
        return userTelegramId;
    }

    public Long getUserChatId() {
        return userChatId;
    }

    public String getPhone() {
        return phone;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(userTelegramId, user.userTelegramId) && Objects.equals(userChatId, user.userChatId) && Objects.equals(status, user.status) && Objects.equals(phone, user.phone) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, userTelegramId, userChatId, status, phone, role);
    }


}
