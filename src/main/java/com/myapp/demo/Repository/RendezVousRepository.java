package com.myapp.demo.Repository;

import com.myapp.demo.dto.RdvDTO;
import com.myapp.demo.entity.Consultation;
import com.myapp.demo.entity.RendezVous;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

	@Query("""
			  select r from RendezVous r
			  where r.cabinetId = :doc
			    and r.status = 'BOOKED'
			    and r.startAt <= :end and r.endAt >= :start
			""")
	List<RendezVous> findActiveByCabinetIdAndRange(@Param("doc") Long cabinetId, @Param("start") Instant start,
			@Param("end") Instant end);

	@Query("""
			  select r from RendezVous r
			  where r.cabinetId = :doc
			    and r.startAt <= :end and r.endAt >= :start
			""")
	List<RendezVous> findAllByCabinetIdAndRange(@Param("doc") Long cabinetId, @Param("start") Instant start,
			@Param("end") Instant end);

	
	

	Optional<RendezVous> findByClientUuid(String clientUuid);

	Page<RendezVous> findByUpdatedAtAfterOrderByUpdatedAtAsc(Instant since, Pageable pageable);
	Page<RendezVous> findByUpdatedAtAfter(Instant updatedSince, Pageable pageable);
	//Page<RendezVous> findByUpdatedAtAfter(Instant since, Pageable paging);
}
