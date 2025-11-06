package com.myapp.demo.service;

import com.myapp.demo.Repository.IConsultationRepository;
import com.myapp.demo.entity.Consultation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService implements IConsultationService {

  @Autowired
  IConsultationRepository consultationRepo;

  @Override
  public List<Consultation> getAllConsultations() {
    return consultationRepo.findAll();
  }

  @Override
  public Consultation getConsultationById(Integer id) {
    return consultationRepo.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public Consultation add(Consultation c) {
    return consultationRepo.save(c);
  }

  @Override
  @Transactional
  public Consultation update(Consultation c) {
    return consultationRepo.save(c);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    consultationRepo.deleteById(id);
  }

  @Override
  public List<Consultation> findByPatient(Integer idPatient) {
    return consultationRepo.findByIdPatient(idPatient);
  }

  @Override
  public Page<Consultation> findByPatientPaged(Integer idPatient, Pageable pageable) {
    return consultationRepo.findByIdPatientOrderByUpdatedAtDesc(idPatient, pageable);
  }

  @Override
  public Page<Consultation> findUpdatedSince(Instant since, Pageable pageable) {
    return consultationRepo.findByUpdatedAtAfterOrderByUpdatedAtAsc(since, pageable);
  }

  @Override
  public Consultation createIdempotent(Consultation c) {
    String cu = c.getClientUuid();
    if (cu != null && !cu.isBlank()) {
      return consultationRepo.findByClientUuid(cu).orElseGet(() -> consultationRepo.save(c));
    }
    return consultationRepo.save(c);
  }

  @Override
  public Optional<Consultation> findByClientUuid(String clientUuid) {
    return consultationRepo.findByClientUuid(clientUuid);
  }
}
