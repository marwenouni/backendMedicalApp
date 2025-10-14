package com.myapp.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.myapp.demo.Repository.IPatientRepository;
import com.myapp.demo.controller.PatientsEventsController;
import com.myapp.demo.entity.Patient;

import jakarta.transaction.Transactional;

@Service
public class PatientService implements IPatientService {

	List<Patient> patient;
	@Autowired
	IPatientRepository patientRepo;
	@Autowired 
	PatientsEventsController sse;

	public PatientService() {

	}

	public List<Patient> getPatients() {
		return patientRepo.findAll();
	}
	
	public Patient getPatientById(int id) {
		return patientRepo.getById(id);
	}

	public Page<Patient> findAllPatientByFilter(Pageable pageable) {
		// Fetch the complete list of patients (or from another service)

		// Create a sublist for the given page
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		Sort sort = pageable.getSort();
		int startItem = currentPage * pageSize;
		List<Patient> list;

		Pageable sortAndFilter = PageRequest.of(currentPage, pageSize, sort);
		return patientRepo.findAll(sortAndFilter);

	}
	
	@Override
	public List<Patient> findAllPatientByFirstName(String firstname) {
		
		return patientRepo.findByFirstNameStartingWith(firstname);

	}

	@Override
	public Page<Patient> getPatientsStartingWith(String firstname, Pageable pageable) {
		// Fetch the complete list of patients (or from another service)
		List<Patient> patients = patientRepo.findByFirstNameStartingWith(firstname);

		// Create a sublist for the given page
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		Sort sort = pageable.getSort();
		int startItem = currentPage * pageSize;
		List<Patient> list;

		if (patients.size() < startItem) {
			list = List.of(); // Return an empty list if startItem is beyond list size
		} else {
			int toIndex = Math.min(startItem + pageSize, patients.size());
			list = patients.subList(startItem, toIndex);
		}

		// Create the Page object
		Pageable sortAndFilter = PageRequest.of(currentPage, pageSize, sort);
		return new PageImpl<>(list, sortAndFilter, patients.size());
	}

	@Override
	public Page<Patient> getPatientsByFirstNameContaining(String firstname, Pageable pageable) {
		// Fetch the complete list of patients (or from another service)

		// Create a sublist for the given page
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();

		Sort sort = pageable.getSort();
		int startItem = currentPage * pageSize;
		List<Patient> list;
		List<Patient> patients = patientRepo.getPatientsByFirstNameContaining(firstname);

		if (patients.size() < startItem) {
			list = List.of(); // Return an empty list if startItem is beyond list size
		} else {
			int toIndex = Math.min(startItem + pageSize, patients.size());
			list = patients.subList(startItem, toIndex);
		}

		// Create the Page object

		Pageable sortAndFilter = PageRequest.of(currentPage, pageSize, sort);
		return new PageImpl<>(list, sortAndFilter, patients.size());
	}

	@Override
	public Page<Patient> findByBirthdayStartingWith(String birthday, Pageable pageable) {
		// Fetch the complete list of patients (or from another service)

		List<Patient> patients = patientRepo.findByBirthdayStartingWith(birthday);

		// Create a sublist for the given page
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Patient> list;

		if (patients.size() < startItem) {
			list = List.of();
		} else {
			int toIndex = Math.min(startItem + pageSize, patients.size());
			list = patients.subList(startItem, toIndex);
		}

		// Create the Page object
		return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), patients.size());
	}

	/**
	 * mixte search between firstname and lastname.
	 *
	 * @param chaine     de caractére
	 * @param pagination
	 * @return list of patients
	 */
	@Override
	public Page<Patient> getPatientsByFirstNameAndLastNameContaining(String firstname, String lastname,
			Pageable pageable, String sort, String filter) {
		// Fetch the complete list of patients (or from another service)
		Set<Patient> set, set2;
		if (!(lastname.length() > 0)) {
			List<Patient> patientsByFirstName = patientRepo.findByFirstNameContainingAndLastNameContaining(firstname,
					lastname);
			List<Patient> patientsByLastName = patientRepo.findByFirstNameContainingAndLastNameContaining(lastname,
					firstname);
			set = new HashSet<>(patientsByFirstName);
			set.addAll(patientsByLastName);
		} else {
			List<Patient> patientsByFirstName = patientRepo.findByFirstName(firstname);
			List<Patient> patientsByLastName = patientRepo.findByLastName(firstname);

			List<Patient> patientsByFirstName2 = patientRepo.findByFirstNameStartingWith(lastname);
			List<Patient> patientsByLastName2 = patientRepo.findByLastNameStartingWith(lastname);

			set = new HashSet<>(patientsByFirstName);
			set.addAll(patientsByLastName);

			set2 = new HashSet<>(patientsByFirstName2);
			set2.addAll(patientsByLastName2);

			set.retainAll(set2);
		}

		// Convertir l'ensemble en une liste
		List<Patient> patients = new ArrayList<>(set);
		List<Patient> sortedList=null;
		// Sort the list by Patient ID
		switch (filter) {
		case "FirstName": {

			sortedList = patients.stream().sorted(Comparator.comparing(Patient::getFirstName))
					.collect(Collectors.toList());
		}
		case "Birthday": {

			sortedList = patients.stream().sorted(Comparator.comparing(Patient::getBirthday))
					.collect(Collectors.toList());
		}
		case "City": {

			sortedList = patients.stream().sorted(Comparator.comparing(Patient::getCity))
					.collect(Collectors.toList());
		}
		case "Id": {

			sortedList = patients.stream().sorted(Comparator.comparing(Patient::getId))
					.collect(Collectors.toList());
		}}
		
		
		if (sort.equals("DESC")) {
			//sortedList = sortedList.reversed();
		}

		// Create a sublist for the given page
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Patient> pageList;

		if (sortedList.size() < startItem) {
			pageList = List.of(); // Return an empty list if startItem is beyond list size
		} else {
			int toIndex = Math.min(startItem + pageSize, sortedList.size());
			pageList = sortedList.subList(startItem, toIndex);
		}

		// Create the Page object
		return new PageImpl<>(pageList, PageRequest.of(currentPage, pageSize), sortedList.size());

	}
	
	@Override
	public Patient add(Patient p) {
		Patient saved = patientRepo.save(p);
	    sse.notifyPatientsChanged();
	    return saved;
			}
	
	@Override
	  @Transactional
	  public Patient update(Patient p) {
	    Patient saved = patientRepo.save(p);
	    sse.notifyPatientsChanged();
	    return saved;
	  }
	
	@Override
	  @Transactional
	  public void delete(int id) {
		patientRepo.deleteById(id);
	    sse.notifyPatientsChanged();
	  }

	@Override
	 public Patient createIdempotent(Patient p) {
		    String cu = p.getClientUuid();
		    if (cu != null && !cu.isBlank()) {
		      return patientRepo.findByClientUuid(cu).orElseGet(() -> patientRepo.save(p));
		    }
		    return patientRepo.save(p);
		  }
	 
	@Override
	public List<Patient> findAllPatientByLastName(String lastname) {
		return patientRepo.findByLastNameContaining(lastname);
	}
	
	@Override
	public List<Patient> findAllPatientByBirthday(String birthday) {
		return patientRepo.findByBirthday(birthday);
	}

	@Override
	public List<Patient> findAllPatientByPhoneNumber(String phonenumber) {
		return patientRepo.findByNumberPhone(phonenumber);
	}
	
	

	

}
