package com.skypro.shelteranimaltgbot.controller;

import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.model.enums.RoleEnum;
import com.skypro.shelteranimaltgbot.repository.UserRepository;
import com.skypro.shelteranimaltgbot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * UserController
 * Контроллер для обработки REST-запросов, в данном случае добавления, удаления, редактирования и поиска пользователей
 *
 * @see UserService
 */
@RestController
@RequestMapping("users")
public class UserController {

    /**
     * Поле сервиса пользователя
     */
    private final UserService userService;

    /**
     * Конструктор - создание нового объекта сервиса пользователя
     *
     * @param userService
     * @see UserService(UserRepository)
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Функция добавления нового пользователя{@link UserService#addUser(User)}
     *
     * @param user
     * @return возвращает объект, содержащий данные созданного пользователя
     */
    @Operation(
            summary = "Функция добавления нового пользователя в базу данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление пользователя в базу данных",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class)
                    )
            ))
    @PostMapping   //POST http://localhost:8080/users
    public ResponseEntity<User> addUser(@RequestBody User user)  {
        return ResponseEntity.ok(userService.addUser(user));
    }

    /**
     * Функция получения пользователя по идентификатору (id), хранящихся в базе данных {@link UserService#findUser(Long)}
     *
     * @param id идентификатор пользователя, не может быть null
     * @return возвращает пользователя по идентификатору (id)
     */
    @Operation(
            summary = "Функция получения пользователя по идентификатору из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденый пользователь",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )})
    @GetMapping(path = "{id}")   //GET http://localhost:8080/users/{id}
    public ResponseEntity<User> findUser(//@Parameter(description = "Ввод id пользователя", name = "ID пользователя")
                                         @PathVariable Long id) {
        User user = userService.findUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Функция получения всех пользователей, хранящихся в базе данныех
     *
     * @return возвращает всех пользователей
     */
    @Operation(
            summary = "Получение списка всех пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение всех пользователей",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    )
            }
    )
    @GetMapping(path = "/all")   //GET http://localhost:8080/users/all
    public ResponseEntity<Collection<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    /**
     * Функция редактирования данных пользователя
     *
     * @param user редактируемый пользователь
     * @return отредактированный пользователь
     */
    @Operation(
            summary = "редактирование данных пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отредактированный пользователь",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class))
                    )
            }
    )
    @PutMapping    //PUT http://ocalhost:8080/users
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User foundUser = userService.updateUser(user);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);
    }

    @Operation(
            summary = "Удаление  пользователя из базы данных по идентификатору (id)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаленный пользователь",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь с таким id, не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class))
                    )
            }
    )
    @DeleteMapping(path = "/{id}")   //DELETE http://localhost:8080/users/{id}
    public ResponseEntity<Void> deleteUser(//@Parameter(description = "Ввод id пользователя", name = "ID пользователя")
                                           @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/update-user-role/{id}/{role}")
    // PATCH http://localhost:8080/users//update-user-role/{id}/{role}
    public ResponseEntity<User> updateUserRole(@PathVariable("id") Long idUser, @PathVariable("role") RoleEnum roleEnum) {
        return ResponseEntity.ok(userService.updateUserRole(idUser, roleEnum));
    }

}
