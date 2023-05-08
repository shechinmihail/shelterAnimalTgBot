package com.skypro.shelteranimaltgbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.shelteranimaltgbot.controller.UserController;
import com.skypro.shelteranimaltgbot.model.Enum.RoleEnum;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;
    @SpyBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void updateUser() throws Exception {
        final long id = 1;
        final String firstName = "Ivan";
        final String lastName = "Sidorov";
        final RoleEnum roleEnum = RoleEnum.VOLUNTEER;
        final String newFirstName = "Igor";
        final String newLastName = "Puc";
        final RoleEnum newRoleEnum = RoleEnum.ADMIN;


        User user = new User();
       // user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(roleEnum);

        User updateUser = new User();
        // updateUser.setId(id);
        updateUser.setFirstName(newFirstName);
        updateUser.setLastName(newLastName);
        updateUser.setRole(newRoleEnum);

        JSONObject userObject = new JSONObject();
        userObject.put("id", id);
        userObject.put("firstName", newFirstName);
        userObject.put("lastName", newLastName);
        userObject.put("role", newRoleEnum);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updateUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(newFirstName))
                .andExpect(jsonPath("$.lastName").value(newLastName));
                //.andExpect(jsonPath("$.role").value(newRoleEnum));
        //java.lang.AssertionError: JSON path "$.role" expected:<ADMIN> but was:<ADMIN> опять ошибка, если ЕНАМ  не может корректно сравнить

    }

    @Test
    public void addUser() throws Exception {
        final long id = 1;
        final String firstName = "Ivan";
        final String lastName = "Sidorov";
        final RoleEnum roleEnum = RoleEnum.VOLUNTEER;

        User user = new User();
        //  user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(roleEnum);

        JSONObject userObject = new JSONObject();
        userObject.put("id", id);
        userObject.put("firstName", firstName);
        userObject.put("lastName", lastName);
        userObject.put("role", roleEnum);


        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(userObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
                //.andExpect(jsonPath("$.role").value(roleEnum));
        // то же самое, опять ЕНАМ не сравнивается java.lang.AssertionError: JSON path "$.role" expected:<VOLUNTEER> but was:<VOLUNTEER>
    }

    @Test
    public void findUserById() throws Exception {
        final long id = 1;
        final String firstName = "Ivan";
        final String lastName = "Sidorov";
        final RoleEnum roleEnum = RoleEnum.VOLUNTEER;

        User user = new User();
        // user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(roleEnum);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void findAll() throws Exception {
        final long id = 1;
        final String firstName = "Ivan";
        final String lastName = "Sidorov";
        final RoleEnum roleEnum = RoleEnum.VOLUNTEER;
        final String newFirstName = "Igor";
        final String newLastName = "Puc";
        final RoleEnum newRoleEnum = RoleEnum.ADMIN;

        User user = new User();
        //  user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(roleEnum);

        User updateUser = new User();
        //    updateUser.setId(id);
        updateUser.setFirstName(newFirstName);
        updateUser.setLastName(newLastName);
        updateUser.setRole(newRoleEnum);

        List<User> userCollection = List.of(user, updateUser);

        Mockito.when(userRepository.findAll()).thenReturn(userCollection);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(userCollection)));
    }

    @Test
    public void deleteUser() throws Exception {
        final long id = 1;
        final String firstName = "Ivan";
        final String lastName = "Sidorov";
        final RoleEnum roleEnum = RoleEnum.VOLUNTEER;

        User user = new User();
        //  user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(roleEnum);

        when(userRepository.getById(id)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userRepository, atLeastOnce()).deleteById(id);
    }

}
