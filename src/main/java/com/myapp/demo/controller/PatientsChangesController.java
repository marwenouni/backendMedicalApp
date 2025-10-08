package com.myapp.demo.controller;

import com.myapp.demo.Repository.IPatientRepository;
import com.myapp.demo.Repository.PatientChangeRepository;
import com.myapp.demo.dto.PatientDelta;
import com.myapp.demo.dto.PatientDto;
import com.myapp.demo.entity.Patient;
import com.myapp.demo.entity.PatientChange;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/patient")
@CrossOrigin
public class PatientsChangesController {

  private final PatientChangeRepository changesRepo;
  private final IPatientRepository patientRepo;

  public PatientsChangesController(PatientChangeRepository changesRepo, IPatientRepository patientRepo) {
    this.changesRepo = changesRepo;
    this.patientRepo = patientRepo;
  }

  @GetMapping("/changes")
  public PatientDelta changes(@RequestParam("since") long since,
                              @RequestParam(value = "limit", defaultValue = "500") int limit) {

    var page    = PageRequest.of(0, limit, Sort.by("changeId").ascending());
    var changes = changesRepo.findByChangeIdGreaterThanOrderByChangeIdAsc(since, page).getContent();

    // ids à upserter / supprimer
    List<Long> idsUpsert = changes.stream()
        .filter(c -> !"D".equals(c.getOp()))
        .map(PatientChange::getRowId)
        .distinct()
        .toList();

    List<Long> idsDelete = changes.stream()
        .filter(c ->  "D".equals(c.getOp()))
        .map(PatientChange::getRowId)
        .distinct()
        .toList();

    // cast Long -> Integer pour le repository de Patient
    List<Integer> idsUpsertInt = idsUpsert.stream()
        .map(Long::intValue)
        .toList();

    List<Long> idsUpsertLong = idsUpsert; // c'est déjà des Long
    List<Patient> patients = idsUpsertLong.isEmpty()
        ? List.of()
        : patientRepo.findAllById(idsUpsertLong);

    List<PatientDto> upserts = patients.stream()
        .map(PatientDto::from) // <-- nécessite PatientDto.from(...)
        .toList();

    long next    = changes.isEmpty() ? since : changes.get(changes.size() - 1).getChangeId();
    boolean more = changes.size() == limit;

    return new PatientDelta(since, next, upserts, idsDelete, more);
  }
}
