package com.myapp.demo.patients.app.interfaces;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myapp.demo.patients.api.dto.PatientDto;

public interface IPatientService {
	

	List<PatientDto> getPatients();

	Page<PatientDto> getPatientsStartingWith(String firstName,Pageable paging);

	Page<PatientDto> getPatientsByFirstNameContaining(String firstName,Pageable paging);
	
    Page<PatientDto> findAllPatientByFilter(Pageable pageable);
	
	
	/**
	 * mixte search between firstname and lastname.
	 *
	 * @param chaine     de caractére
	 * @param pagination
	 * @return list of patients
	 */
	Page<PatientDto> getPatientsByFirstNameAndLastNameContaining(String firstname, String lastname, Pageable pageable,
			String sort, String filter);

	List<PatientDto> findAllPatientByIdCabinet(Long id);
	

	List<PatientDto> findAllPatientByIdProvider(Long idProvider);

	Optional<PatientDto> getPatientById(Long id);

	List<PatientDto> findAllPatientByFirstName(String firstname);
	
	List<PatientDto> findAllPatientByLastName(String firstname);
	
	List<PatientDto> findAllPatientByPhoneMobile(String phonenumber);

	void delete(int id);

	Optional<PatientDto> findByClientUuid(String uuid);

	List<PatientDto> findUpdated(Instant since);


	List<PatientDto> findAllPatientByBirthday(LocalDate birthday);

	PatientDto add(PatientDto dto);

	PatientDto createIdempotent(PatientDto dto);

	PatientDto update(PatientDto dto);


	




		
}
