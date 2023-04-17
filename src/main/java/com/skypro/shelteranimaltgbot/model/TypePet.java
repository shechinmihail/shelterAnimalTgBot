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

    //@ManyToMany(fetch = FetchType.EAGER, mappedBy = "typePets", cascade = CascadeType.ALL)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Document> documentsList;

    public TypePet() {
    }

    public TypePet(String type, Document document) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }


    public void setDocuments(Set<Document> documents) {
        this.documentsList = documents;
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
