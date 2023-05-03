package com.skypro.shelteranimaltgbot.controller;


import com.skypro.shelteranimaltgbot.model.Enum.ReportStatus;
import com.skypro.shelteranimaltgbot.model.Report;
import com.skypro.shelteranimaltgbot.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * ReportController
 * Контроллер для обработки REST-запросов, в данном случае добавления, удаления, редактирования и поиска отчета о домашних питомцев
 *
 * @see ReportService
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Добавление отчета о домашнем питомце в базу данных",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавленный отчет о домашнем питомце",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Report.class)
                    )
            )
    )
    @PostMapping     //POST http://localhost:8080/report
    public Report createReport(@RequestBody Report report) {
        return reportService.createReport(report);
    }

    @Operation(
            summary = "Поиск отчета о домашнем питомце в базе данных по идентификатору (id)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный отчет о домашнем питомце",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет о домашнем питомце не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    )
            }
    )
    @GetMapping("/{id}")  //GET http://localhost:8080/report/{id}
    public ResponseEntity<Report> findReport(@PathVariable Long id) {
        Report report = reportService.findReport(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }

    @Operation(
            summary = "Редактирование отчета о домашнем питомце в базе данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отредактированный отчет о домашнем питомце",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет о домашнем питомце не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    )
            }
    )
    @PutMapping    //PUT http://ocalhost:8080/report
    public ResponseEntity<Report> updateReport(@RequestBody Report report,
                                               @Parameter(description = "Статус отчета", example = "ACCEPTED")
                                               @RequestParam(name = "Status") ReportStatus reportStatus) {
        Report reportPet = reportService.updateReport(report, reportStatus);
        if (reportPet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reportPet);
    }

    @Operation(
            summary = "Удаление отчета о домашнем питомце из базы данных по идентификатору (id)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаленный отчет о домашнем питомце",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчет о домашнем питомце не найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class))
                    )
            }
    )
    @DeleteMapping("/{id}")    //DELETE http://localhost:8080/report/{id}
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение списка всех отчетов по домашним питомцам",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение всех отчетов по домашним питомцам",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping(path = "all")   //GET http://localhost:8080/report/all
    public ResponseEntity<Collection<Report>> getAllReport() {
        return ResponseEntity.ok(reportService.getAllReport());
    }


}
