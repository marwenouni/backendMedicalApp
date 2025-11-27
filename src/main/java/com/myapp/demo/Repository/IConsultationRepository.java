package com.myapp.demo.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.Consultation;


public interface IConsultationRepository  extends JpaRepository<Consultation, Long> {
	
	List<Consultation> findAll();

	Consultation getById(Integer id);

	void deleteById(Integer id);

	List<Consultation> findByIdPatient(Integer idPatient);

	Page<Consultation> findByIdPatientOrderByUpdatedAtDesc(Integer idPatient, Pageable pageable);

	Page<Consultation> findByUpdatedAtAfterOrderByUpdatedAtAsc(Instant since, Pageable pageable);

	Optional<Consultation> findByClientUuid(String cu);

	Optional<Consultation> findById(Integer id);

	Page<Consultation> findByUpdatedAtAfter(Instant since, Pageable paging);

}
