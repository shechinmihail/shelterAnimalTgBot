package com.skypro.shelteranimaltgbot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "status_list")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    public Status(String status) {
        this.status = status;
    }

    public Status() {
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status that = (Status) o;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "StatusList{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}