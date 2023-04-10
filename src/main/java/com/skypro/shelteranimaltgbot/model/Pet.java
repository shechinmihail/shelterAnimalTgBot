package com.skypro.shelteranimaltgbot.model;


import com.skypro.shelteranimaltgbot.model.Enum.StatusPet;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс Pet, представляет сущность домашнего питомца
 */
@Entity
@Table(name = "pet")
public class Pet {

    /**
     * Идентификационный номер (id) домашнего питомца
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Имя домашнего питомца
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Возраст домашнего питомца
     */
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * Вид домашнего питомца
     */
    @Column(name = "typeOfPet")
    private String typeOfPet;

    /**
     * Статус домашнего питомца
     *
     * @see StatusPet
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPet statusPet;

    /**
     * Конструктор для создания объекта домашний питомец
     *
     * @param name      имя домашнего питомца
     * @param age       возраст домашнего питомца
     * @param typeOfPet вид домашнего питомца
     * @param statusPet статус домашнего питомца
     */
    public Pet(String name, Integer age, String typeOfPet, StatusPet statusPet) {
        this.name = name;
        this.age = age;
        this.typeOfPet = typeOfPet;
        this.statusPet = statusPet;
    }

    /**
     * Конструктор для создания объекта домашний питомец, без параметров
     */
    public Pet() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTypeOfPet() {
        return typeOfPet;
    }

    public void setTypeOfPet(String typeOfPet) {
        this.typeOfPet = typeOfPet;
    }

    public StatusPet getStatusPet() {
        return statusPet;
    }

    public void setStatusPet(StatusPet statusPet) {
        this.statusPet = statusPet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id) && Objects.equals(name, pet.name) && Objects.equals(age, pet.age) && Objects.equals(typeOfPet, pet.typeOfPet) && statusPet == pet.statusPet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, typeOfPet, statusPet);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", typeOfPet='" + typeOfPet + '\'' +
                ", statusPet=" + statusPet +
                '}';
    }
}
