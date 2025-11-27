package com.myapp.demo.charts.infra.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myapp.demo.charts.domain.ChartCore;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ChartCoreRepository extends JpaRepository<ChartCore, Long> {

	@EntityGraph(attributePaths = {"identity", "clinical", "insurance", "consent"})
	Optional<ChartCore> findByClientUuid(String clientUuid);

	@EntityGraph(attributePaths = {"identity", "clinical", "insurance", "consent"})
    List<ChartCore> findByUpdatedAtAfter(Instant since);

	
	  @EntityGraph(attributePaths =  {"identity","clinical","insurance","consent"})
	  List<ChartCore> findAll();
	  
	  @EntityGraph(attributePaths =  {"identity","clinical","insurance","consent"})
	  Page<ChartCore> findAll(Pageable page);

	  @EntityGraph(attributePaths = {"identity","clinical","insurance","consent"})
	  @Query("select c from ChartCore c where c.id = :id")
	  Optional<ChartCore> findWithAllById(Long id);
	  
	  @EntityGraph(attributePaths = {"identity","clinical","insurance","consent"})
	  Page<ChartCore> findAllBy(Pageable pageable);
	  
	  @EntityGraph(attributePaths = {"identity","clinical","insurance","consent"})
	  @Query("select c from ChartCore c where c.id = :id")
	  Optional<ChartCore> findById(Long id);

	  List<ChartCore>  findAllByIdCabinet(Long idCabinet);

	  List<ChartCore>  findAllByIdProvider(Long idProvider);
	  
	  
	
}
