package com.skypro.shelteranimaltgbot.model;


import com.skypro.shelteranimaltgbot.model.Enum.ReportStatus;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс Report, представляет сущность отчета по домашнему питомцу
 */
@Entity
public class Report {

    /**
     * Идентификационный номер (id) отчета по домашнему питомцу
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Телеграм id пользователя
     */
    private Long userTelegramId;

    /**
     * Фотография в отчете
     */
    private String photo;

    /**
     * Рацион питания домашнего питомца
     */
    private String diet;

    /**
     * Информация о домашнем питомце
     */
    private String petInfo;

    /**
     * Описание об изменении поведения и привычек домашнего питомца
     */
    private String changeInPetBehavior;

    /**
     * Дата заполнения отчета
     */
    @NonNull
    private LocalDate date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adoption")
    private Adoption adoption;

    /**
     * Поле: статус отчета
     *
     * @see ReportStatus
     */
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    /**
     * Идентификатор домашнего питомца
     */
//    @ManyToOne
//    @JoinColumn(name = "pet")
//    private Pet pet;
    public Report(Long userTelegramId, String photo, String diet,
                  String petInfo, String changeInPetBehavior) {
        this.userTelegramId = userTelegramId;
        this.photo = photo;
        this.diet = diet;
        this.petInfo = petInfo;
        this.changeInPetBehavior = changeInPetBehavior;
        this.date = LocalDate.now();
    }

    public Report() {
        this.date = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserTelegramId() {
        return userTelegramId;
    }

    public void setUserTelegramId(Long userTelegramId) {
        this.userTelegramId = userTelegramId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getPetInfo() {
        return petInfo;
    }

    public void setPetInfo(String petInfo) {
        this.petInfo = petInfo;
    }

    public String getChangeInPetBehavior() {
        return changeInPetBehavior;
    }

    public void setChangeInPetBehavior(String changeInPetBehavior) {
        this.changeInPetBehavior = changeInPetBehavior;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(userTelegramId, report.userTelegramId)
                && Objects.equals(photo, report.photo) && Objects.equals(diet, report.diet)
                && Objects.equals(petInfo, report.petInfo) && Objects.equals(changeInPetBehavior, report.changeInPetBehavior)
                && Objects.equals(date, report.date) && reportStatus == report.reportStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userTelegramId, photo, diet, petInfo, changeInPetBehavior, date, reportStatus);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userTelegramId=" + userTelegramId +
                ", photo='" + photo + '\'' +
                ", diet='" + diet + '\'' +
                ", petInfo='" + petInfo + '\'' +
                ", changeInPetBehavior='" + changeInPetBehavior + '\'' +
                ", date=" + date +
                ", reportStatus=" + reportStatus +
                '}';
    }
}
