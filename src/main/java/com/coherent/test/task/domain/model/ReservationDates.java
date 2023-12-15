package com.coherent.test.task.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ReservationDates {

    @Id
    @Column(name = "res_date_id")
    private Integer resDateId;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @ManyToOne
    @JoinColumn(name = "reservationId", referencedColumnName = "reservation_id", nullable = false)
    private ReservationEntity reservation;

}
