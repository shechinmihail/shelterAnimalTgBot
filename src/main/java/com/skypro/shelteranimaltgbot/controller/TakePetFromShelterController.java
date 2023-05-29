package com.skypro.shelteranimaltgbot.controller;

import com.skypro.shelteranimaltgbot.model.TakePetFromShelter;
import com.skypro.shelteranimaltgbot.service.TakePetFromShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * TakePetFromShelter
 * Контроллер для обработки REST-запросов, в данном случае добавления, удаления, редактирования и поиска рекомендаций как взять животное
 *
 * @see TakePetFromShelterService
 */
@RestController
@RequestMapping("take_pet")
public class TakePetFromShelterController {

    private final TakePetFromShelterService takePetFromShelterService;

    public TakePetFromShelterController(TakePetFromShelterService takePetFromShelterService) {
        this.takePetFromShelterService = takePetFromShelterService;
    }

    /**
     * Функция добавления новой рекомендации {@link TakePetFromShelterService#addDescription(TakePetFromShelter)}
     *
     * @param takePetFromShelter
     * @return возвращает объект, содержащий рекомендации по тому как взять животное из приюта
     */
    @Operation(
            summary = "Функция добавления новой рекомендации в базу данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление рекомендации в базу данных",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TakePetFromShelter.class)
                    )
            ))
    @PostMapping   //POST http://localhost:8080/take_pet
    public ResponseEntity<TakePetFromShelter> addDescription(@RequestBody TakePetFromShelter takePetFromShelter)  {
        return ResponseEntity.ok(takePetFromShelterService.addDescription(takePetFromShelter));
    }

    /**
     * Функция получения рекомендации по идентификатору (id), хранящихся в базе данных
     *
     * @param id идентификатор рекомендации, не может быть null
     * @return возвращает рекомендацию по идентификатору (id)
     */
    @Operation(
            summary = "Функция получения рекомендации по идентификатору из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденное описание",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TakePetFromShelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рекомендация не найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TakePetFromShelter.class)
                            )
                    )})
    @GetMapping(path = "/{id}")   //GET http://localhost:8080/take_pet/{id}
    public ResponseEntity<TakePetFromShelter> findDescription(@Parameter(description = "Ввод id рекомендации", name = "ID рекомендации")
                                         @PathVariable Long id) {
        TakePetFromShelter takePetFromShelter = takePetFromShelterService.findDescription(id);
        if (takePetFromShelter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(takePetFromShelter);
    }

    /**
     * Функция получения всех рекомендаций, хранящихся в базе данныех
     *
     * @return возвращает все рекомендации
     */
    @Operation(
            summary = "Получение списка всех рекомендаций",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение всех рекомендаций",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TakePetFromShelter.class))
                            )
                    )
            }
    )
    @GetMapping(path = "all")   //GET http://localhost:8080/take_pet/all
    public ResponseEntity<Collection<TakePetFromShelter>> getAll() {
        return ResponseEntity.ok(takePetFromShelterService.getAll());
    }

    /**
     * Функция редактирования рекомендаицй
     *
     * @param takePetFromShelter редактируемая рекомендация
     * @return отредактированная рекомендация
     */
    @Operation(
            summary = "редактирование рекомендации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отредактированная рекомендация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TakePetFromShelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рекомендация не найдена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TakePetFromShelter.class))
                    )
            }
    )
    @PutMapping    //PUT http://localhost:8080/take_pet
    public ResponseEntity<TakePetFromShelter> updateRecomendation(@RequestBody TakePetFromShelter takePetFromShelter) {
        TakePetFromShelter foundRecomendation = takePetFromShelterService.updateDescription(takePetFromShelter);
        if (foundRecomendation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundRecomendation);
    }

    /**
     * Функция удаления рекомендации
     *
     * @param id рекомендации, которую нужно удалить
     * @return удаленная рекомендация
     */
    @Operation(
            summary = "Удаление  рекомендациии из базы данных по идентификатору (id)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаленная рекомендация",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TakePetFromShelter.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Рекомендации с таким id, не существует",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TakePetFromShelter.class))
                    )
            }
    )
    @DeleteMapping(path = "/{id}")   //DELETE http://localhost:8080/take_pet/{id}
    public ResponseEntity<Void> deleteRecomendation(@Parameter(description = "Ввод id рекомендации", name = "ID рекомендации")
                                           @PathVariable Long id) {
        takePetFromShelterService.deleteRecomendation(id);
        return ResponseEntity.ok().build();
    }
}
