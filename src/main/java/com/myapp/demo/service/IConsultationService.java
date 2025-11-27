package com.myapp.demo.service;

import com.myapp.demo.entity.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IConsultationService {
	List<Consultation> getAllConsultations();

	Consultation getConsultationById(Integer id);

	Consultation add(Consultation c);

	Consultation update(Consultation c);

	void delete(Integer id);

	List<Consultation> findByChart(Integer idChart);

	Page<Consultation> findByChartPaged(Integer idChart, Pageable pageable);

	Page<Consultation> findUpdatedSince(Instant since, Pageable pageable);

	Consultation createIdempotent(Consultation c);

	Optional<Consultation> findByClientUuid(String clientUuid);

	Page<Consultation> findByUpdatedAtAfter(Instant since, Pageable paging);

	Page<Consultation> findAll(Pageable paging);
}
