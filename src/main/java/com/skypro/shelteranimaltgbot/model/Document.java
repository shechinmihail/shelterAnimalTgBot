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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "documentsList", cascade = CascadeType.ALL)
    private Set<TypePet> typePets;


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

    public Set<TypePet> getTypePets() {
        return typePets;
    }

    public void setTypePets(Set<TypePet> typePets) {
        this.typePets = typePets;
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
                ", typePets=" + typePets +
                '}';
    }
}

