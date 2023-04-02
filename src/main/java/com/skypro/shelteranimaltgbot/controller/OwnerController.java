package com.skypro.shelteranimaltgbot.controller;

import com.skypro.shelteranimaltgbot.model.Owner;
import com.skypro.shelteranimaltgbot.repository.OwnerRepository;
import com.skypro.shelteranimaltgbot.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * OwnerController
 * Контроллер для обработки REST-запросов, в данном случае добавление и поиск владельцев
 *
 * @see OwnerService
 */
@RestController
@RequestMapping("owner")
public class OwnerController {

    /**
     * Поле сервиса владельца
     */
    @Autowired
    private final OwnerService ownerService;

    /**
     * Конструктор - создание нового объекта сервиса
     *
     * @param ownerService
     * @see OwnerService(OwnerRepository)
     */
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Функция добавления нового владельца домашнего питомца{@link OwnerService#addOwner(Owner)}
     *
     * @param owner Владелец
     * @return возвращает объект, содержащий данные созданного владельца
     */
    @Operation(
            summary = "Функция добавления нового владельца домашнего питомца в базу данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавленный владелец в базу данных",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Owner.class)
                    )
            ))
    @PostMapping//POST http://localhost:8080/owner
    public Owner addOwner(@RequestBody Owner owner) {
        return ownerService.addOwner(owner);
    }

    /**
     * Функция получения владельца по идентификатору (id), хранящихся в базе данных {@link OwnerService#findOwner(Long)}
     *
     * @param id идентификатор владельца, не может быть null
     * @return возвращает владельца домашнего питомца по идентификатору (id)
     */
    @Operation(
            summary = "Функция получения владельца домашнего питомца по идентификатору из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденый владелец домашнего питомца",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Owner.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Владелец домашнего питомца не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Owner.class)
                            )
                    )})
    @GetMapping(path = "{id}")   //GET http://localhost:8080/owner/{id}
    public ResponseEntity<Owner> findOwner(@PathVariable Long id) {
        Owner owner = ownerService.findOwner(id);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(owner);
    }
}
