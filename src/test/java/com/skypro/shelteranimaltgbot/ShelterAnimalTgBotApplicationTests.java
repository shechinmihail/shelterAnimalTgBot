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
        jsonObject.put("first_name", "TEST");
        jsonObject.put("last_name", "TEST");
        jsonObject.put("phone", "123");
        jsonObject.put("role", "User");
        jsonObject.put("status","GUEST");
        jsonObject.put("user_telegram_id", "1");
        mockMvc
                .perform(
                        post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("TEST"))
                .andExpect(jsonPath("$[0].lastName").value("TEST"))
                .andExpect(jsonPath("$[0].phone").value("123"))
                .andExpect(jsonPath("$[0].role").value("User"))
                .andExpect(jsonPath("$[0].status").value("GUEST"))
                .andExpect(jsonPath("$[0].userTelegramId").value("1"));
    }
}
