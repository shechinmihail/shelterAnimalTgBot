package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Класс TypePet, представляет сущность типа животного
 */
@Entity
@Table(name = "type_pet")
public class TypePet {

    /**
     * Идентификационный номер (id) типа животного
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип животного
     */
    private String type;

    /**
     * Список документов для типа животного
     */
    @OneToMany(mappedBy = "typePetDoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Document> documentsList;

    /**
     * Список правил/рекомендаций для типа животного
     */
    @OneToMany(mappedBy = "typePetRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TakePetFromShelter> takePetFromShelters;

    public TypePet() {
    }

    public TypePet(String type) {
        this.type = type;
    }

    public TypePet(String type, Set<Document> documents, Set<TakePetFromShelter> takePetFromShelters) {
        this.type = type;
        this.documentsList = documents;
        this.takePetFromShelters = takePetFromShelters;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Set<Document> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(Set<Document> documentsList) {
        this.documentsList = documentsList;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<TakePetFromShelter> getTakePetFromShelters() {
        return takePetFromShelters;
    }

    public void setTakePetFromShelters(Set<TakePetFromShelter> takePetFromShelters) {
        this.takePetFromShelters = takePetFromShelters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypePet typePet = (TypePet) o;
        return Objects.equals(id, typePet.id) && Objects.equals(type, typePet.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "TypePet{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", documentsList=" + documentsList +
                ", takePetFromShelters=" + takePetFromShelters +
                '}';
    }
}
