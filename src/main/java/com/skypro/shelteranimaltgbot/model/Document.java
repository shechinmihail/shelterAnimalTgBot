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


    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private Set<TypePet> typePets;

    private String document;

    public Document() {
    }

    public Document(Set<TypePet> typePets, String document) {
        this.typePets = typePets;
        this.document = document;
    }

    public Long getId() {
        return id;
    }

    public Set<TypePet> getTypePets() {
        return typePets;
    }

    public String getDocument() {
        return document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document1 = (Document) o;
        return Objects.equals(id, document1.id) && Objects.equals(typePets, document1.typePets) && Objects.equals(document, document1.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typePets, document);
    }
}

