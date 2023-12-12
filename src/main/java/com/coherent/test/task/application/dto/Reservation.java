package com.coherent.test.task.application.dto;

import java.time.LocalDate;
import java.util.List;

public record Reservation(int id, String clientFullName, int roomNumber, List<LocalDate> reservationDates) {
}
