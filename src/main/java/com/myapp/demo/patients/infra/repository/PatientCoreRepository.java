package com.myapp.demo.patients.infra.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myapp.demo.patients.domain.PatientCore;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PatientCoreRepository extends JpaRepository<PatientCore, Long> {

	@EntityGraph(attributePaths = {"identity", "clinical", "insurance", "consent"})
	Optional<PatientCore> findByClientUuid(String clientUuid);

	@EntityGraph(attributePaths = {"identity", "clinical", "insurance", "consent"})
    List<PatientCore> findByUpdatedAtAfter(Instant since);

	
	  @EntityGraph(attributePaths =  {"identity","clinical","insurance","consent"})
	  List<PatientCore> findAll();
	  
	  @EntityGraph(attributePaths =  {"identity","clinical","insurance","consent"})
	  Page<PatientCore> findAll(Pageable page);

	  @EntityGraph(attributePaths = {"identity","clinical","insurance","consent"})
	  @Query("select c from PatientCore c where c.id = :id")
	  Optional<PatientCore> findWithAllById(Long id);
	  
	  @EntityGraph(attributePaths = {"identity","clinical","insurance","consent"})
	  Page<PatientCore> findAllBy(Pageable pageable);
	  
	  @EntityGraph(attributePaths = {"identity","clinical","insurance","consent"})
	  @Query("select c from PatientCore c where c.id = :id")
	  Optional<PatientCore> findById(Long id);

	  List<PatientCore>  findAllByIdCabinet(Long idCabinet);

	  List<PatientCore>  findAllByIdProvider(Long idProvider);
	  
	  
	
}
