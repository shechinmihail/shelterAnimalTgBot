package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "take_pets")
public class TakePetFromShelter {

    /**
     * Идентификационный номер (id) описания
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Правило/рекомендация
     */
    private String nameRule;

    /**
     * Описание как взять животное
     */
    private String description;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_pet_id")
    private TypePet typePetRule;

    public TakePetFromShelter() {
    }

    public TakePetFromShelter(String nameRule, String description, TypePet typePetRule) {
        this.nameRule = nameRule;
        this.description = description;
        this.typePetRule = typePetRule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getNameRule() {
        return nameRule;
    }

    public void setNameRule(String nameRule) {
        this.nameRule = nameRule;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TakePetFromShelter that = (TakePetFromShelter) o;
        return Objects.equals(id, that.id) && Objects.equals(nameRule, that.nameRule) && Objects.equals(description, that.description) && Objects.equals(typePetRule, that.typePetRule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameRule, description, typePetRule);
    }

    @Override
    public String toString() {
        return "TakePetFromShelter{" +
                "id=" + id +
                ", nameRule='" + nameRule + '\'' +
                ", description='" + description + '\'' +
                ", typePetRule=" + typePetRule +
                '}';
    }
}
