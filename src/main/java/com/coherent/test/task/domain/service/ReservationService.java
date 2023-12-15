package com.coherent.test.task.domain.service;

import com.coherent.test.task.application.dto.Error;
import com.coherent.test.task.application.dto.Reservation;
import io.vavr.control.Either;

import java.util.Set;

public interface ReservationService {

    Either<Error, Set<Reservation>> createReservation(Reservation reservation);

    Either<Error, Set<Reservation>> getAllReservations();

    Either<Error, Set<Reservation>> updateReservationById(Reservation reservation, int id);

    void saveDataInDB();

}
