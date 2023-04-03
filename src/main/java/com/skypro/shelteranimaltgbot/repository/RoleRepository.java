package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
