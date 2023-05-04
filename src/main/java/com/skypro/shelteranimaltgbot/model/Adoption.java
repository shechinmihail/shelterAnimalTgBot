package com.skypro.shelteranimaltgbot.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * класс Adoption предназначен для того, чтобы зафиксировать процесс усыновления питомца,
 * Волонтер создает новую запись в БД, дата записи является отправной точкой для мониторинга
 * процесса адаптации питомца
 */

@Entity
public class Adoption {

    /**
     * Идентификационный номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * усыновитель
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * питомец
     */
    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * дата усыновления
     */
    @NonNull
    private LocalDate date;

    /**
     * список отчетов по усыновлению
     */
    @OneToMany(mappedBy = "adoption", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Report> reports;
    private Integer countDayAdoption;

    public Adoption() {
        this.date = LocalDate.now();
    }


    public Adoption(User user, Pet pet, Set<Report> reports, Integer countDayAdoption) {
        this.user = user;
        this.pet = pet;
        this.date = LocalDate.now();
        this.reports = reports;
        this.countDayAdoption = countDayAdoption;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Integer getCountDayAdoption() {
        return countDayAdoption;
    }

    public void setCountDayAdoption(Integer countDayAdoption) {
        this.countDayAdoption = countDayAdoption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adoption adoption = (Adoption) o;
        return Objects.equals(id, adoption.id) && Objects.equals(user, adoption.user) && Objects.equals(pet, adoption.pet) && date.equals(adoption.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, pet, date);
    }

    @Override
    public String toString() {
        return "Adoption{" +
                "id=" + id +
                ", user=" + user +
                ", pet=" + pet +
                ", date=" + date +
                ", reports=" + reports +
                ", countDayAdoption=" + countDayAdoption +
                '}';
    }
}
