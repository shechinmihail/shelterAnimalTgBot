package com.skypro.shelteranimaltgbot.model;

import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Enum.StatusEnum;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс User, представляет сущность пользователя
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Идентификационный номер (id) пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Телеграм id пользователя
     */
    private Long userTelegramId;


    /**
     * Статус пользователя
     */
    private StatusEnum status;

    /**
     * Номер телефона пользователя
     */
    private String phone;

    /**
     * Роль пользователя
     */
    private RoleEnum role;

    public User() {
    }

    /**
     * Конструктор для создания объекта пользователь
     *
     * @param firstName      Имя пользователя
     * @param lastName       Фамилия пользователя
     * @param userTelegramId Телеграм id пользователя
     * @param status         Статус пользователя
     * @param role           Роль пользователя
     */
    public User(String firstName, String lastName, Long userTelegramId, Long userChatId, StatusEnum status, RoleEnum role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userTelegramId = userTelegramId;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(userTelegramId, user.userTelegramId) && Objects.equals(status, user.status) && Objects.equals(phone, user.phone) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, userTelegramId, status, phone, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userTelegramId=" + userTelegramId +
                ", status=" + status +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }
}
