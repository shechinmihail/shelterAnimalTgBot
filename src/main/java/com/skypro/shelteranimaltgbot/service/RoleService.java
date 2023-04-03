package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.Role;
import com.skypro.shelteranimaltgbot.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole () {
        Set<Role> roleList = new HashSet<>(roleRepository.findAll());
        if (!roleList.contains(new Role(RoleEnum.VOLUNTEER))) {
            roleRepository.save(new Role (RoleEnum.VOLUNTEER));
        }
    }

}