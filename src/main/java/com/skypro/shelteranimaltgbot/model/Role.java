package com.skypro.shelteranimaltgbot.model;

import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private RoleEnum role;

    public Role() {
    }

    public Role(RoleEnum role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public RoleEnum getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(id, role1.id) && Objects.equals(role, role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role=" + role +
                '}';
    }
}