package com.skypro.shelteranimaltgbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.shelteranimaltgbot.controller.PetController;
import com.skypro.shelteranimaltgbot.model.Document;
import com.skypro.shelteranimaltgbot.model.Enum.StatusPet;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
public class PetServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetRepository petRepository;

    @InjectMocks
    private PetController petController;
    @SpyBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void updatePet() throws Exception {
        final long id = 1;
        final StatusPet statusPet = StatusPet.FREE;
        final StatusPet newStatusPet = StatusPet.BUSY;


        Pet pet = new Pet();
        //pet.setId(id);
        pet.setStatusPet(statusPet);

        Pet updatedPet = new Pet();
       // updatedPet.setId(id);
        updatedPet.setStatusPet(newStatusPet);

        JSONObject petObject = new JSONObject();
        petObject.put("id", id);
        petObject.put("statusPet", newStatusPet);

        when(petRepository.findById(id)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedPet);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet?Статус=" + newStatusPet)
                        .content(petObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.statusPet").value(newStatusPet));
        // ??? java.lang.AssertionError: JSON path "$.statusPet" expected:<BUSY> but was:<BUSY> ??? это ошибка теста

    }

    @Test
    public void addPet() throws Exception {
        final String name = "Ball";
        final Integer age = 2;
        final long id = 1;
       // final TypePet typePet = new TypePet("Dog", new Document("Passport"));
        final StatusPet statusPet = StatusPet.FREE;

        Pet pet = new Pet();
      //  pet.setTypePet(typePet);
       // pet.setId(id);
        pet.setAge(age);
        pet.setName(name);
        pet.setStatusPet(statusPet);

        JSONObject petObject = new JSONObject();
        petObject.put("id", id);
        petObject.put("name", name);
        petObject.put("age", age);
     //   petObject.put("typePet", typePet);
        petObject.put("statusPet", statusPet);


        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet?Статус=" + statusPet)
                        .content(petObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // приходит 400, вместо 200, как будто запрос неверный отправляю, хотя делаю его по аналогии с тестом update
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
         //       .andExpect(jsonPath("$.typePet").value(typePet))
                .andExpect(jsonPath("$.statusPet").value(statusPet));
    }

    @Test
    public void findPetById() throws Exception {
        final String name = "Tiger";
        final Integer age = 2;
        final long id = 1;
        final StatusPet statusPet = StatusPet.FREE;

        Pet pet = new Pet();
        pet.setName(name);
        pet.setAge(age);
        pet.setStatusPet(statusPet);


        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pet)));
    }

    @Test
    public void findAll() throws Exception {
        final String name = "Tiger";
        final Integer age = 2;
        final long id = 1;
        final String name2 = "Sharik";
        final Integer age2 = 3;
        final long id2 = 2;

        Pet pet = new Pet();
      //  pet.setId(id);
        pet.setName(name);
        pet.setAge(age);

        Pet pet2 = new Pet();
      //  pet2.setId(id2);
        pet2.setName(name2);
        pet2.setAge(age2);

        List<Pet> petCollection = List.of(pet, pet2);

        Mockito.when(petRepository.findAll()).thenReturn(petCollection);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(petCollection)));
    }

    @Test
    public void deletePet() throws Exception {
        final String name = "Tiger";
        final Integer age = 2;
        final long id = 1;

        Pet pet = new Pet();
     //   pet.setId(id);
        pet.setName(name);
        pet.setAge(age);

        when(petRepository.getById(id)).thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pet/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(petRepository, atLeastOnce()).deleteById(id);
    }

}
