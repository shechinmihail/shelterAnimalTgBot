package com.skypro.shelteranimaltgbot;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ShelterAnimalTgBotApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void testUser() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "1");
        jsonObject.put("firstName", "TEST");
        jsonObject.put("lastName", "TEST");
        jsonObject.put("userTelegramId", "1");
        jsonObject.put("status", "GUEST");
        jsonObject.put("phone", "123");
        jsonObject.put("role", "USER");

        mockMvc
                .perform(
                        post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/users/1"))
                .andExpectAll(
                        jsonPath("$.size()").value(7),
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.firstName").value("TEST"),
                        jsonPath("$.lastName").value("TEST"),
                        jsonPath("$.phone").value("123"),
                        jsonPath("$.role").value("USER"),
                        jsonPath("$.status").value("GUEST"),
                        jsonPath("$.userTelegramId").value("1")

                );

        mockMvc
                .perform(
                        put("/users").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());

        mockMvc.
                perform
                        (get("/users/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk(),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].firstName").value("TEST"),
                        jsonPath("$[0].lastName").value("TEST"),
                        jsonPath("$[0].phone").value("123"),
                        jsonPath("$[0].role").value("USER"),
                        jsonPath("$[0].status").value("GUEST"),
                        jsonPath("$[0].userTelegramId").value("1")

                );

        mockMvc
                .perform(
                        delete("/users/1").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testPet() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "2");
        jsonObject.put("typePet", "1");
        jsonObject.put("name", "TEST");
        jsonObject.put("age", "5");
        jsonObject.put("statusPet", "BUSY");
        jsonObject.put("filePath", "photo");

        mockMvc
                .perform(
                        post("/pet").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pet/2"))
                .andExpectAll(
                        jsonPath("$.size()").value(5),
                        status().isOk(),
                        jsonPath("$.id").value(2),
                        jsonPath("$.typePet").value("1"),
                        jsonPath("$.name").value("TEST"),
                        jsonPath("$.age").value("5"),
                        jsonPath("$.statusPet").value("BUSY"),
                        jsonPath("$.filePath").value("photo")
                );

        mockMvc
                .perform(
                        put("/pet").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());

        mockMvc.
                perform
                        (get("/pet/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk(),
                        jsonPath("$[0].id").value(2),
                        jsonPath("$[0].typePet").value("1"),
                        jsonPath("$[0].name").value("TEST"),
                        jsonPath("$[0].age").value("5"),
                        jsonPath("$[0].statusPet").value("BUSY"),
                        jsonPath("$[0].filePath").value("photo")
                );

        mockMvc
                .perform(
                        delete("/pet/2").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
    }
}
