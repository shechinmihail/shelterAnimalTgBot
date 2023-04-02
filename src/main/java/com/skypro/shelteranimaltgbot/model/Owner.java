package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс Owner, представляет сущность владельца домашнего питомца
 */
@Entity
@Table(name = "owner")
public class Owner {

    /**
     * Идентификационный номер (id) владельца домашнего питомца
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Номер чата владельца
     */
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    /**
     * Имя владельца домашнего питомца
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Номер телефона владельца домашнего питомца
     */
    @Column(name = "phone", nullable = false)
    private String phone;

    /**
     * Возразт владельца домашнего питомца
     */
    @Column(name = "age", nullable = false)
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return id == owner.id && chatId == owner.chatId && age == owner.age && Objects.equals(name, owner.name) && Objects.equals(phone, owner.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, name, phone, age);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                '}';
    }
}
