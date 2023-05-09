package com.skypro.shelteranimaltgbot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String i = "1";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", i); //нужен для pet
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
        mockMvc.perform(get("/users/" + i))
                .andExpectAll(
                        jsonPath("$.size()").value(7),
                        status().isOk(),
                        jsonPath("$.id").value(i),
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
        mockMvc.perform(get("/users/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk(),
                        jsonPath("$[0].id").value(i),
                        jsonPath("$[0].firstName").value("TEST"),
                        jsonPath("$[0].lastName").value("TEST"),
                        jsonPath("$[0].phone").value("123"),
                        jsonPath("$[0].role").value("USER"),
                        jsonPath("$[0].status").value("GUEST"),
                        jsonPath("$[0].userTelegramId").value("1")
                );

        mockMvc
                .perform(
                        delete("/users/" + i).contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testPet() throws Exception {
        String i = "1";
        JSONObject jsonObjectDocument = new JSONObject();
        jsonObjectDocument.put("id", "1");
        jsonObjectDocument.put("document", "Паспорт");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObjectDocument);

        JSONObject jsonObjectTypePet = new JSONObject();
        jsonObjectTypePet.put("id", "1");
        jsonObjectTypePet.put("type", "Кошки");
        jsonObjectTypePet.put("documentsList", jsonArray);

        JSONObject jsonObjectPet = new JSONObject();
        jsonObjectPet.put("id", i);
        jsonObjectPet.put("name", "test");
        jsonObjectPet.put("age", "1");
        jsonObjectPet.put("typePet", jsonObjectTypePet);
        jsonObjectPet.put("statusPet", "FREE");

        mockMvc
                .perform(
                        post("/pet").contentType(MediaType.APPLICATION_JSON).content(jsonObjectPet.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(
                        put("/pet").param("Status", "BUSY").contentType(MediaType.APPLICATION_JSON).content(jsonObjectPet.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/pet/" + i))
                .andExpectAll(
                        jsonPath("$.size()").value(6),
                        status().isOk(),
                        jsonPath("$.id").value(i)
                );
        mockMvc.perform(get("/pet/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk()
                );
        mockMvc
                .perform(
                        delete("/pet/" + i).contentType(MediaType.APPLICATION_JSON).content(jsonObjectPet.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(
                        get("/pet/{petId}"))
                .andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk()
                );
    }

    @Test
    void AdoptionTest() throws Exception{
        String i = "1";
        Long userId = 1L;
        Long petId = 2L;
        Integer trialPeriod = 7;
        boolean LocalDate = false;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", i);
        jsonObject.put("userId", userId);
        jsonObject.put("petId", petId);
        jsonObject.put("date", LocalDate);
        jsonObject.put("trialPeriod",trialPeriod);

        mockMvc
                .perform(
                        post("/adoption").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/adoption/" + i))
                .andExpectAll(
                        jsonPath("$.size()").value(7),
                        status().isOk(),
                        jsonPath("$.id").value(i)
                );
        mockMvc.perform(get("/adoption/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk()
                );
        mockMvc
                .perform(
                        delete("/adoption/" + i).contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(
                        put("/adoption/").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk()); // не проходит проверка, редактирования записи
    }

    @Test
    public void createRecordTest() {
        Long userId = 1L;
        Long petId = 2L;
        Integer trialPeriod = 7;
        RestTemplate restTemplate = new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(), new StringHttpMessageConverter())
                .build();
        String port = String.valueOf(8082);
        String url = "http://localhost:" + port + "/adoption/create-an-adoption-record/" + userId + "/" + petId + "/" + trialPeriod;
        
           ResponseEntity response = restTemplate.postForEntity(url, null, ResponseEntity.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
