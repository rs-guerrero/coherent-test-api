package com.coherent.test.task.application.controller;

import com.coherent.test.task.application.dto.Reservation;
import com.coherent.test.task.application.dto.Response;
import com.coherent.test.task.domain.service.ReservationService;
import com.coherent.test.task.infrastructure.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "reservation", description = "Reservation API")
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService service;

    @GetMapping
    @Operation(summary = "Find all Reservations", description = "Returns all reservations", tags = {"reservation"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "reservation not found", content = @Content)})
    public ResponseEntity<Response> getAllReservations() {
        return service.getAllReservations().fold(
                ControllerUtils::getResponseError,
                ControllerUtils::getResponseSuccessOk
        );
    }

    @PostMapping
    @Operation(summary = "Create Reservation", description = "Create reservation and then return all reservations", tags = {"reservation"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful Created", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "reservation not found", content = @Content)})
    public ResponseEntity<Response> createReservation(@NonNull @RequestBody Reservation reservation) {
        return service.createReservation(reservation).fold(
                ControllerUtils::getResponseError,
                ControllerUtils::getResponseSuccessOk
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "update reservation", description = "Update reservation and then return all reservations", tags = {"reservation"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "supplied ID not found", content = @Content)})
    public ResponseEntity<Response> updateReservation(@PathVariable int id, @NonNull @RequestBody Reservation reservation) {
        return service.updateReservationById(reservation, id).fold(
                ControllerUtils::getResponseError,
                ControllerUtils::getResponseSuccessOk
        );
    }

}
