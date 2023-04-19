package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Shelter;
import com.skypro.shelteranimaltgbot.repository.ShelterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    @Mock
    ShelterRepository shelterRepository;

    @InjectMocks
    ShelterService shelterService;


    @Test
    void getAbout() {
        Shelter shelter = new Shelter("test", "test", "test");
        when(shelterRepository.findByUserName(anyString())).thenReturn(shelter);
        Shelter shelterNew = new Shelter("test", "null", "test");
        Assertions.assertEquals("test", shelterService.getAbout("test"));
    }

    @Test
    void getAboutNotNull() {
        Shelter shelter = new Shelter("test", "test", null);
        when(shelterRepository.findByUserName(anyString())).thenReturn(shelter);
        Shelter shelterNew = new Shelter("test", "null", "test");
        Assertions.assertEquals("Описания нет", shelterService.getAbout("test"));
    }
}