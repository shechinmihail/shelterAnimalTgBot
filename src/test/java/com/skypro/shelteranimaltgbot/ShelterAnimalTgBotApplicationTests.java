package com.skypro.shelteranimaltgbot;

import com.skypro.shelteranimaltgbot.model.TypePet;
import com.skypro.shelteranimaltgbot.repository.TypePetRepository;
import com.skypro.shelteranimaltgbot.service.TypePetService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ShelterAnimalTgBotApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TypePetService typePetService;
    @Autowired
    private TypePetRepository typePetRepository;

    private final JSONObject jsonObjectUser = new JSONObject();
    private final JSONObject jsonObjectDocument = new JSONObject();
    private final JSONObject jsonObjectTypePet = new JSONObject();
    private final JSONObject jsonObjectPet = new JSONObject();
    private final JSONObject jsonObjectAdoption = new JSONObject();
    private final JSONObject jsonObjectAdoption2 = new JSONObject();
    private final JSONObject jsonObjectTakeRecommendation = new JSONObject();
    private final JSONObject jsonObjectReport = new JSONObject();
    private final String i = "1";
    private final String userId = "1";
    private final String petId = "1";
    private final String trialPeriod = "7";
    private final String nameRule = "test";
    private final String description = "test";
    private final String typePetRule = "1";
    private final String userTelegramId = "1";
    private final String photo = null;
    private final String diet = "test";
    private final String petInfo = "test";
    private final String changeInPetBehavior = "test";
    private final LocalDate date = LocalDate.now();


    @BeforeEach
    void setUp() throws JSONException {

        //User
        jsonObjectUser.put("id", i);
        jsonObjectUser.put("firstName", "TEST");
        jsonObjectUser.put("lastName", "TEST");
        jsonObjectUser.put("userTelegramId", i);
        jsonObjectUser.put("status", "GUEST");
        jsonObjectUser.put("phone", "123");
        jsonObjectUser.put("role", "USER");

        //Document
        jsonObjectDocument.put("id", i);
        jsonObjectDocument.put("document", "Паспорт");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObjectDocument);

        //TypePet
        jsonObjectTypePet.put("id", i);
        jsonObjectTypePet.put("type", "Кошки");
        jsonObjectTypePet.put("documentsList", jsonArray);

        //Pet
        jsonObjectPet.put("id", 2);
        jsonObjectPet.put("name", "test");
        jsonObjectPet.put("age", i);
        jsonObjectPet.put("typePet", jsonObjectTypePet);
        jsonObjectPet.put("statusPet", "FREE");

        //Adoption
        jsonObjectAdoption.put("id", i);
        jsonObjectAdoption.put("userId", userId);
        jsonObjectAdoption.put("petId", petId);
        jsonObjectAdoption.put("trialPeriod", trialPeriod);


        //TakePet
        jsonObjectTakeRecommendation.put("id", "1");
        jsonObjectTakeRecommendation.put("nameRule", nameRule);
        jsonObjectTakeRecommendation.put("description", description);
        jsonObjectTakeRecommendation.put("typePetRule", typePetRule);

        //Adoption 2

        jsonObjectAdoption2.put("id", i);
        jsonObjectAdoption2.put("user", jsonObjectUser);
        jsonObjectAdoption2.put("pet", jsonObjectPet);
        jsonObjectAdoption2.put("trialPeriod", trialPeriod);

        //Report

        jsonObjectReport.put("id", i);
        jsonObjectReport.put("userTelegramId", userTelegramId);
        jsonObjectReport.put("photo", photo);
        jsonObjectReport.put("diet", diet);
        jsonObjectReport.put("petInfo", petInfo);
        jsonObjectReport.put("changeInPetBehavior", changeInPetBehavior);
        jsonObjectReport.put("date", date);


    }


    @Test
    void testUser() throws Exception {

        mockMvc
                .perform(
                        post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonObjectUser.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/users/" + i))
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
                        put("/users").contentType(MediaType.APPLICATION_JSON).content(jsonObjectUser.toString()))
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
                        jsonPath("$[0].userTelegramId").value(i)
                );

        mockMvc
                .perform(
                        delete("/users/" + 1).contentType(MediaType.APPLICATION_JSON).content(jsonObjectUser.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testPet() throws Exception {
        typePetRepository.save(new TypePet("Кошки"));
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
                        jsonPath("$.size()").value(7),
                        status().isOk(),
                        jsonPath("$.id").value(i)
                );
        mockMvc.perform(get("/pet/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(2),
                        status().isOk()
                );
        mockMvc
                .perform(
                        delete("/pet/" + 2).contentType(MediaType.APPLICATION_JSON).content(jsonObjectPet.toString()))
                .andExpect(status().isOk());

    }

    @Test
    void AdoptionTest() throws Exception {

        mockMvc
                .perform(
                        post("/adoption").contentType(MediaType.APPLICATION_JSON).content(jsonObjectAdoption.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(
                        put("/adoption")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectAdoption.toString()))
                .andExpect(status().isOk()); // не проходит проверка, редактирования записи
        mockMvc
                .perform(
                        get("/adoption/" + i))
                .andExpectAll(
                        jsonPath("$.size()").value(7),
                        status().isOk(),
                        jsonPath("$.id").value(i)
                );
        mockMvc
                .perform(
                        get("/adoption/all"))
                .andExpectAll(
                        jsonPath("$.size()").value(2),
                        status().isOk()
                );
        mockMvc
                .perform(
                        delete("/adoption/" + i).contentType(MediaType.APPLICATION_JSON).content(jsonObjectAdoption.toString()))
                .andExpect(status().isOk());

    }

    @Test
    public void createRecordTest() throws Exception {
        typePetRepository.save(new TypePet("Кошки"));

        mockMvc
                .perform(
                        post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonObjectUser.toString()))
                .andExpect(status().isOk());

        mockMvc
                .perform(
                        post("/pet").contentType(MediaType.APPLICATION_JSON).content(jsonObjectPet.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(
                        post("/adoption").contentType(MediaType.APPLICATION_JSON).content(jsonObjectAdoption.toString()))
                .andExpect(status().isOk());


        mockMvc
                .perform(post("/adoption/create-an-adoption-record/" + userId + "/" + petId + "/" + trialPeriod).contentType(MediaType.APPLICATION_JSON).content(jsonObjectAdoption2.toString()))
                .andExpect(status().isOk());

    }

    @Test
    public void reportControllerTest() throws Exception {

        mockMvc
                .perform(
                        post("/report").contentType(MediaType.APPLICATION_JSON).content(jsonObjectReport.toString()))
                .andExpect(status().isOk());
        mockMvc
                .perform(
                        get("/report/1")
                ).andExpectAll(
                        jsonPath("$.size()").value(9),
                        status().isOk(),
                        jsonPath("$.id").value(1)
                );
        mockMvc
                .perform(
                        put("/report").param("Status", "ACCEPTED").contentType(MediaType.APPLICATION_JSON).content(jsonObjectReport.toString())
                ).andExpect(status().isOk());
        mockMvc
                .perform(
                        get("/report/all")
                ).andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk()
                );
        mockMvc
                .perform(delete("/report/1")
                ).andExpect(status().isOk());

    }

    @Test
    public void TakePetFromShelterControllerTest() throws Exception {
        typePetRepository.save(new TypePet("Кошки"));

        mockMvc
                .perform(
                        post("/take_pet").contentType(MediaType.APPLICATION_JSON).content(jsonObjectTakeRecommendation.toString())
                ).andExpect(status().isOk());
        mockMvc
                .perform(
                        get("/take_pet/1")
                ).andExpectAll(
                        jsonPath("$.size()").value(3),
                        status().isOk(),
                        jsonPath("$.id").value(1)
                );
        mockMvc
                .perform(
                        get("/take_pet/all")
                ).andExpectAll(
                        jsonPath("$.size()").value(1),
                        status().isOk()
                );
        mockMvc
                .perform(
                        put("/take_pet").contentType(MediaType.APPLICATION_JSON).content(jsonObjectTakeRecommendation.toString())
                ).andExpect(status().isOk());
        mockMvc
                .perform(
                        delete("/take_pet/1")
                ).andExpect(status().isOk());

    }
}
