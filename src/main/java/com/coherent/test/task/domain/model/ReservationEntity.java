package com.coherent.test.task.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @Column(name = "reservation_id")
    private int reservationId;

    @Column(name = "client_full_name")
    private String clientFullName;

    @Column(name = "room_number")
    private int roomNumber;


}
