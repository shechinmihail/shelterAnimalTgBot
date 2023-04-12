package com.skypro.shelteranimaltgbot.repository;


import com.skypro.shelteranimaltgbot.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    @Query("select s.about from Shelter s where s.userName = :userNameShelter")
    String findAboutByUserName(@Param("userNameShelter") String usernameShelter);

}