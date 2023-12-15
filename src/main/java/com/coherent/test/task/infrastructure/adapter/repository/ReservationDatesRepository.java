package com.coherent.test.task.infrastructure.adapter.repository;

import com.coherent.test.task.domain.model.ReservationDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDatesRepository extends JpaRepository<ReservationDates, Long> {

    List<ReservationDates> findByReservationReservationId(int id);

}
