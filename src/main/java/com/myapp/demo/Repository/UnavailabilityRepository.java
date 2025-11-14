package com.myapp.demo.Repository;


import com.myapp.demo.entity.Unavailability;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface UnavailabilityRepository extends JpaRepository<Unavailability, Long> {
  @Query("""
    select u from Unavailability u
    where u.cabinetId = :doc and u.startAt <= :end and u.endAt >= :start
  """)
  List<Unavailability> findByCabinetIdAndRange(@Param("doc") Long cabinetId,
                                              @Param("start") Instant start,
                                              @Param("end") Instant end);
}

