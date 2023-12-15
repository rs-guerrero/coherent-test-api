package com.coherent.test.task.infrastructure.adapter.repository;

import com.coherent.test.task.domain.model.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

}
