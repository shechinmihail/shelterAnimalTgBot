package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@Entity
public class TypePet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @OneToMany(mappedBy = "typePet", cascade = CascadeType.ALL)
    private Set<Document> documentsList;
    public TypePet() {
    }

    public TypePet(String type) {
        this.type = type;
    }

    public TypePet(String type, Set<Document> documents) {
        this.type = type;
        this.documentsList = documents;
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
                '}';
    }
}
