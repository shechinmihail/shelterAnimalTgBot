package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String document;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_pet_id")
    private TypePet typePet;

    public Document() {
    }

    public Document(String document) {
        this.document = document;
    }

    public Long getId() {
        return id;
    }

    public String getDocument() {
        return document;
    }

    public TypePet getTypePet() {
        return typePet;
    }

    public void setTypePets(TypePet typePet) {
        this.typePet = typePet;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document1 = (Document) o;
        return Objects.equals(id, document1.id) && Objects.equals(document, document1.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, document);
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", document='" + document + '\'' +
                //", typePet=" + typePet +
                '}';
    }
}

