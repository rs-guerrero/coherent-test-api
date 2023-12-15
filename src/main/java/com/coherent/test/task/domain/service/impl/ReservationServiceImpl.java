package com.coherent.test.task.domain.service.impl;

import com.coherent.test.task.application.dto.Error;
import com.coherent.test.task.application.dto.Reservation;
import com.coherent.test.task.domain.model.ReservationDates;
import com.coherent.test.task.domain.model.ReservationEntity;
import com.coherent.test.task.domain.service.ReservationService;
import com.coherent.test.task.infrastructure.adapter.repository.ReservationDatesRepository;
import com.coherent.test.task.infrastructure.adapter.repository.ReservationRepository;
import io.vavr.control.Either;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationDatesRepository reservationDatesRepository;

    private Set<Reservation> reservations;

    // Load data from repositories on application startup
    @PostConstruct
    public void loadDataOnStartup() {
        log.info("Loading data on application startup...");
        reservations = reservationRepository.findAll()
                .stream()
                .map(item -> {
                    var resDates = reservationDatesRepository.findByReservationReservationId(item.getReservationId())
                            .stream().map(ReservationDates::getReservationDate).toList();
                    return new Reservation(item.getReservationId(), item.getClientFullName(), item.getRoomNumber(), resDates);
                }).collect(Collectors.toSet());
        log.info("Data loaded successfully.");
    }

    @Override
    public Either<Error, Set<Reservation>> createReservation(Reservation reservation) {
        // Validation for required fields in Reservation
        if (StringUtils.isEmpty(reservation.clientFullName()) || reservation.roomNumber() == 0 || reservation.reservationDates().isEmpty()) {
            log.error("Invalid reservation data received for creation: {}", reservation);
            return Either.left(Error.builder()
                    .message("All the element of Reservation must be present")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build());
        }
        // Add reservation to the in-memory set and save it to the database
        log.info("Creating reservation: {}", reservation);
        reservations.add(reservation);

        log.info("Creating reservation: {}", reservation);
        return getAllReservations();

    }

    @Override
    public Either<Error, Set<Reservation>> getAllReservations() {
        // Check if reservations are empty and return an error if so
        if (reservations.isEmpty()) {
            log.warn("No reservations found");
            return Either.left(Error.builder()
                    .message("Reservations not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build());
        }
        log.info("Returning all reservations: {}", reservations);
        return Either.right(reservations);

    }


    @Override
    public Either<Error, Set<Reservation>> updateReservationById(Reservation dto, int id) {
        // Validation for required fields in Reservation
        if (StringUtils.isEmpty(dto.clientFullName()) || dto.roomNumber() == 0 || dto.reservationDates().isEmpty()) {
            log.error("Invalid reservation data received for update: {}", dto);
            return Either.left(Error.builder()
                    .message("All the element of Reservation must be present")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build());
        }
        var atomicBoolean = new AtomicBoolean(Boolean.FALSE);

        Iterator<Reservation> iterator = reservations.iterator();
        while (iterator.hasNext()) {
            Reservation item = iterator.next();
            if (item.id() == id) {
                atomicBoolean.set(Boolean.TRUE);
                iterator.remove();

            }
        }
        if (!atomicBoolean.get()) {
            log.warn("Reservation with ID {} not found for update", id);
            return Either.left(Error.builder()
                    .message("Supplied ID not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build());
        }
        log.info("Reservation with ID {} updated. New data: {}", id, dto);
        reservations.add(dto);
        return getAllReservations();

    }

    // Save data to the database on application shutdown
   @Override
    public void saveDataInDB() {
        if (!reservations.isEmpty()) {
            log.info("Saving data to the database on application shutdown...");
            AtomicInteger accumulator = new AtomicInteger(0);

            reservations.forEach(item -> {
                ReservationEntity entity = new ReservationEntity();
                entity.setReservationId(item.id());
                entity.setClientFullName(item.clientFullName());
                entity.setRoomNumber(item.roomNumber());
                reservationRepository.save(entity);

                item.reservationDates().forEach(x -> {
                    ReservationDates rd = new ReservationDates();
                    rd.setReservation(entity);
                    rd.setResDateId(accumulator.addAndGet(1));
                    rd.setReservationDate(x);
                    reservationDatesRepository.save(rd);
                });
            });
            log.info("Data saved to the database successfully.");
        }
    }
}
