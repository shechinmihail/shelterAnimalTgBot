package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TypePet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_list_id")
    private Document document;

    public TypePet() {
    }

    public TypePet(String type, Document document) {
        this.type = type;
        this.document = document;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypePet typePet = (TypePet) o;
        return Objects.equals(id, typePet.id) && Objects.equals(type, typePet.type) && Objects.equals(document, typePet.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, document);
    }

    @Override
    public String toString() {
        return "TypePet{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", document=" + document +
                '}';
    }
}
