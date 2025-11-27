package com.myapp.demo.patients.api;

import com.myapp.demo.Repository.IPatientRepository;
import com.myapp.demo.Repository.PatientChangeRepository;
import com.myapp.demo.patients.api.dto.PatientDto;
import com.myapp.demo.entity.Patient;
import com.myapp.demo.entity.PatientChange;
import com.myapp.demo.patients.api.dto.PatientDelta;

import org.springframework.beans.factory.annotation.Autowired;
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

//  @GetMapping("/changes")
//  public PatientDelta changes(@RequestParam("since") long since,
//                              @RequestParam(value = "limit", defaultValue = "500") int limit) {
//
//	  var pageReq = PageRequest.of(0, limit, Sort.by("changeId").ascending());
//	  var page    = changesRepo.findByChangeIdGreaterThanOrderByChangeIdAsc(since, pageReq);
//	  var changes = page.getContent();
//	  boolean more = page.hasNext();
//
//    var idsUpsert = changes.stream()
//        .filter(c -> !"D".equalsIgnoreCase(c.getOp()))
//        .map(PatientChange::getRowId)
//        .distinct()
//        .toList();
//
//    var idsDelete = changes.stream()
//        .filter(c ->  "D".equalsIgnoreCase(c.getOp()))
//        .map(PatientChange::getRowId)
//        .distinct()
//        .toList();
//    
//    
//
//    // le PK de Patient est Integer ? Alors caste :
//    // var upsertIdsInt = idsUpsert.stream().map(Long::intValue).toList();
//    // var patients = upsertIdsInt.isEmpty() ? List.<Patient>of() : patientRepo.findAllById(upsertIdsInt);
//
//    // si ton PK Patient est Long, garde Long :
//    var patients = idsUpsert.isEmpty() ? List.<Patient>of() : patientRepo.findAllById(idsUpsert);
//
//    var upserts = patients.stream().map(PatientDto::from).toList();
//
//    long next    = changes.isEmpty() ? since : changes.get(changes.size() - 1).getChangeId();
//     more = changes.size() == limit;
//     
//    return new PatientDelta(since, next, upserts, idsDelete, more);
//  }
}
