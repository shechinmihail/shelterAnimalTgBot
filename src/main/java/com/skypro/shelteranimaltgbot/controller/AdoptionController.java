package com.skypro.shelteranimaltgbot.controller;

import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
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
 * AdoptionController
 * Контроллер для обработки REST-запросов, по добавлению, удалению, редактированию и поиска записей усыновления
 *
 * @see AdoptionService
 */
@RestController
@RequestMapping("adoption")
public class AdoptionController {

    /**
     * поле сервиса записи усыновления
     */
    @Autowired
    AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
    }

    /**
     * Функция добавления новую запись усыновления{@link AdoptionService#addAdoption(Adoption)}
     *
     * @param adoption
     * @return возвращает объект, содержащий данные созданной записи
     */

    @Operation(
            summary = "Добавление записи усыновления питомца в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление записи усыновления",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Adoption.class)
                    )
            )
    )
    @PostMapping    //POST http://localhost:8080/adoption
    public ResponseEntity<Adoption> add(@RequestBody Adoption adoption) {
        return ResponseEntity.ok(adoptionService.addAdoption(adoption));
    }

    /**
     * Функция получения записи усыновления по идентификатору (id), хранящихся в базе данных {@link AdoptionService#findAdoption(Long)}
     *
     * @param id идентификатор записи усыновления, не может быть null
     * @return возвращает запись усыновления по идентификатору (id)
     */
    @Operation(
            summary = "Функция получения записи усыновления питомца по идентификатору (id) из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoption.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Запись не найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoption.class))
                    )
            }
    )

    @GetMapping(path = "/{id}")    //GET http://localhost:8080/adoption/{id}
    public ResponseEntity<Adoption> getAdoption(@PathVariable Long id) {
        Adoption adoption = adoptionService.findAdoption(id);
        if (adoption == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adoption);
    }

    /**
     * Функция редактирования данных записи усыновления
     *
     * @param adoption редактируемая запись усыновления
     * @return отредактированная запись усыновления
     */
    @Operation(
            summary = "Функция редактирования записи усыновления питомца по идентификатору (id) из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoption.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Запись не найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoption.class))
                    )
            }
    )

    @PutMapping  //PUT http://localhost:8080/adoption/
    public ResponseEntity<Adoption> updateAdoption(@RequestBody Adoption adoption) {
        Adoption adoption1 = adoptionService.updateAdoption(adoption);
        if (adoption1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adoption1);

    }

    /**
     * Функция удаления записи усыновления
     *
     * @param id – идентификатор записи усыновления, не может быть null
     */
    @Operation(
            summary = "Удаление  записи усыновления из базы данных по идентификатору (id)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись удалена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoption.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Запись не найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adoption.class))
                    )
            }
    )
    @DeleteMapping(path = "{id}") //DELETE http://localhost:8080/adoption/{id}
    public ResponseEntity<Adoption> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.ok().build();
    }


    /**
     * Функция получения всех записей усыновления, хранящихся в базе данных
     *
     * @return возвращает все записи усыновления
     */
    @Operation(
            summary = "Получение списка всех записей усыновлений питомцев",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получены все записи",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Adoption.class))
                            )
                    )
            }
    )
    @GetMapping("/all")  //GET http://localhost:8080/adoption/all
    public ResponseEntity<Collection<Adoption>> getAdoptions() {
        return ResponseEntity.ok(adoptionService.getAll());
    }

    /**
     * Создание записи в журнал усыновления питомцев
     * После подписания всех документов, Волонтер создает запись усыновления питомца и назаначает количество дней испытательного срока
     *
     * @param userId      - id усыновителя
     * @param petId       - id питомца
     * @param trialPeriod - количество дней испытательного срока
     * @return запись в журнале усыновлений
     **/

    @Operation(
            summary = "Добавление записи усыновления питомца в базу данных с параметрами",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление записи усыновления с параметрами",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Adoption.class)
                    )
            )
    )
    @PostMapping(path = "/create-an-adoption-record/{userId}/{petId}/{trialPeriod}")
    //POST http://localhost:8080/adoption/create-an-adoption-record/{userId}/{petId}/{trialPeriod}
    public ResponseEntity<Adoption> createRecord(
            @PathVariable Long userId,
            @PathVariable Long petId,
            @PathVariable Integer trialPeriod
    ) {
        return ResponseEntity.ok(adoptionService.createRecord(userId, petId, trialPeriod));
    }
}
