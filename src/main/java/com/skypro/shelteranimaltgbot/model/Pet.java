package com.skypro.shelteranimaltgbot.model;


import java.util.Objects;

public class Pet {

    private Long id;
    private String name;
    private Integer age;
    private String breed;

    public Pet(Long id, String name, Integer age, String breed) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id) && Objects.equals(name, pet.name) && Objects.equals(age, pet.age) && Objects.equals(breed, pet.breed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, breed);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", breed='" + breed + '\'' +
                '}';
    }
}
