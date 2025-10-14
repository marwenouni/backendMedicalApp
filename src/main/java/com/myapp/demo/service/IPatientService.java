package com.myapp.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.Patient;

public interface IPatientService {
	

	List<Patient> getPatients();

	Page<Patient> getPatientsStartingWith(String firstName,Pageable paging);

	Page<Patient> getPatientsByFirstNameContaining(String firstName,Pageable paging);
	

	Page<Patient> findByBirthdayStartingWith(String birthday, Pageable pageable);

	Page<Patient> findAllPatientByFilter(Pageable pageable);
	
	
	/**
	 * mixte search between firstname and lastname.
	 *
	 * @param chaine     de caractére
	 * @param pagination
	 * @return list of patients
	 */
	Page<Patient> getPatientsByFirstNameAndLastNameContaining(String firstname, String lastname, Pageable pageable,
			String sort, String filter);



	Patient add(Patient p);

	Patient getPatientById(int id);

	List<Patient> findAllPatientByFirstName(String firstname);
	
	List<Patient> findAllPatientByLastName(String firstname);

	List<Patient> findAllPatientByBirthday(String birthday);
	
	List<Patient> findAllPatientByPhoneNumber(String phonenumber);

	Patient update(Patient p);

	void delete(int id);

	Patient createIdempotent(Patient p);

		
}
