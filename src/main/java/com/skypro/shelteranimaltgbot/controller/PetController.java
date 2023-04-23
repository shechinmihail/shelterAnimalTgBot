package com.skypro.shelteranimaltgbot.controller;


import com.skypro.shelteranimaltgbot.model.Enum.StatusPet;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * PetController
 * Контроллер для обработки REST-запросов, в данном случае добавления, удаления, редактирования и поиска домашних питомцев
 *
 * @see PetService
 */
@RestController
@RequestMapping("pet")
public class PetController {

    /**
     * Поле сервиса домашнего питомца
     */
    @Autowired
    private final PetService petService;

    /**
     * Конструктор - создание нового объекта сервиса домашнего питомца
     *
     * @param petService
     * @see PetService(PetService)
     */
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(
            summary = "Добавление домашнего питомца в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавленный домашний питомец",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class)
                    )
            )
    )
    @PostMapping    //POST http://localhost:8080/pet
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet,
                      @Parameter(description = "Установка статуса домашнего питомца", example = "BUSY")
                      @RequestParam(name = "Статус") StatusPet statusPet) {
        return ResponseEntity.ok(petService.addPet(pet, statusPet));
    }


    @Operation(
            summary = "Функция получения домашнего питомца по идентификатору (id) из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденый домашний питомец",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Домашний питомец не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class))
                    )
            }
    )
    @GetMapping(path = "{id}")   //GET http://localhost:8080/pet/{id}
    public ResponseEntity<Pet> findPet(@Parameter(description = "Ввод id домашнего питомца", name = "ID домашнего питомца")
                                       @PathVariable Long id) {
        Pet pet = petService.findPet(id);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pet);
    }

    @Operation(
            summary = "Получение списка всех домашних питомцев",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение всех домашних питомцев",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    )
            }
    )
    @GetMapping(path = "all")   //GET http://localhost:8080/pet/all
    public ResponseEntity<Collection<Pet>> getAllPet() {
        return ResponseEntity.ok(petService.getAllPet());
    }

    @Operation(
            summary = "редактирование данных домашнего питомца",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отредактированный домашний питомец",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Домашний питомец не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class))
                    )
            }
    )
    @PutMapping   //PUT http://ocalhost:8080/pet
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet,
                                         @Parameter(description = "Установка статуса домашнего питомца", example = "BUSY")
                                         @RequestParam(name = "Статус") StatusPet statusPet) {
        Pet foundPet = petService.updatePet(pet, statusPet);
        if (foundPet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundPet);
    }

    @Operation(
            summary = "Удаление домашнего питомца из базы данных по идентификатору (id)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаленный домашний питомец",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Домашний питомец не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Pet.class))
                    )
            }
    )
    @DeleteMapping(path = "{id}")   //DELETE http://localhost:8080/pet/{id}
    public ResponseEntity<Void> deletePet(@Parameter(description = "Ввод id домашнего питомца", name = "ID домашнего питомца")
                                          @PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok().build();
    }

}
