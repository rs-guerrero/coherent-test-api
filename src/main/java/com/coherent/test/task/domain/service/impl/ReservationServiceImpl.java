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
import lombok.Data;
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

    @PostConstruct
    public void loadDataOnStartup() {
        reservations = reservationRepository.findAll()
                .stream()
                .map(item -> {
                    var resDates = reservationDatesRepository.findByReservationReservationId(item.getReservationId())
                            .stream().map(ReservationDates::getReservationDate).toList();
                    return new Reservation(item.getReservationId(), item.getClientFullName(), item.getRoomNumber(), resDates);
                }).collect(Collectors.toSet());
    }

    @Override
    public Either<Error, Set<Reservation>> createReservation(Reservation reservation) {
        if (StringUtils.isEmpty(reservation.clientFullName()) || reservation.roomNumber() == 0 || reservation.reservationDates().isEmpty()) {
            return Either.left(Error.builder()
                    .message("All the element of Reservation must be present")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build());
        }
        reservations.add(reservation);
        saveReservation(reservation);
        return getAllReservations();

    }

    public void saveReservation(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setReservationId(reservation.id());
        entity.setClientFullName(reservation.clientFullName());
        entity.setRoomNumber(reservation.roomNumber());
        reservationRepository.save(entity);

        AtomicInteger accumulator = new AtomicInteger(0);
        reservation.reservationDates().forEach(item -> {
            ReservationDates rd = new ReservationDates();
            rd.setReservation(entity);
            rd.setResDateId(accumulator.addAndGet(1));
            rd.setReservationDate(item);
            reservationDatesRepository.save(rd);
        });

    }

    @Override
    public Either<Error, Set<Reservation>> getAllReservations() {
        if (reservations.isEmpty()) {
            return Either.left(Error.builder()
                    .message("Reservations not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build());
        }
        return Either.right(reservations);

    }


    @Override
    public Either<Error, Set<Reservation>> updateReservationById(Reservation dto, int id) {
        if (StringUtils.isEmpty(dto.clientFullName()) || dto.roomNumber() == 0 || dto.reservationDates().isEmpty()) {
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
                log.info("Reserva con ID " + id + " eliminada.");
                reservations.add(dto);
            }
        }
        if (!atomicBoolean.get()) {
            return Either.left(Error.builder()
                    .message("Supplied ID not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build());
        }
        return getAllReservations();

    }

    @PreDestroy
    public void saveDataInDB() {
        if (!reservations.isEmpty()) {
            reservationRepository.deleteAll();
            reservationDatesRepository.deleteAll();
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


        }
    }
}
