package com.myapp.demo.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.Patient;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByEmail(String email);

	List<Patient> findAll();

	List<Patient> findByFirstName(String firstname);

	
	List<Patient> findByLastNameStartingWith(String firstname);
	
	List<Patient> getPatientsByFirstNameContaining(String firstname);
	
	List<Patient> getPatientsByLastNameContaining(String firstname);

	List<Patient> findByBirthdayStartingWith(String birthday);

	List<Patient> findByFirstNameAndLastName(String firstname, String firstname2);

	List<Patient> findByFirstNameContainingAndLastNameContaining(String string, String string2);

	List<Patient> findByLastName(String firstname);

	List<Patient> findByFirstNameContainingAndLastName(String firstname, String lastname);

	List<Patient> findByFirstNameContaining(String lastname);

	List<Patient> findByLastNameContaining(String lastname);

	List<Patient> findByFirstNameStartingWith(String firstname);
	
	Patient save(Patient patient);

	Patient getById(int i);

	List<Patient> findByBirthday(String birthday);
	
	List<Patient> findByNumberPhone(String phonenumber);

	

}
