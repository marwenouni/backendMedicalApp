package com.myapp.demo.Repository;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import com.myapp.demo.entity.Patient;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
	
	Patient findByEmail(String email);

	List<Patient> findAll();
	
   Optional<Patient>	 findById(long id);
	
	List<Patient> findByFirstName(String firstname);

	
	List<Patient> findByLastNameStartingWith(String firstname);

	Optional<Patient> findByClientUuid(String clientUuid);
	
	List<Patient> getPatientsByFirstNameContaining(String firstname);
	
	List<Patient> getPatientsByLastNameContaining(String firstname);

	//Page<Patient> findByBirthdayStartingWith(String birthday,Pageable page);

	List<Patient> findByFirstNameAndLastName(String firstname, String firstname2);

	List<Patient> findByFirstNameContainingAndLastNameContaining(String string, String string2);

	List<Patient> findByLastName(String firstname);

	List<Patient> findByFirstNameContainingAndLastName(String firstname, String lastname);

	List<Patient> findByFirstNameContaining(String lastname);

	List<Patient> findByLastNameContaining(String lastname);

	List<Patient> findByFirstNameStartingWith(String firstname);
	
	Patient save(Patient patient);

	Patient getById(int i);

	List<Patient> findByBirthday(LocalDate birthday);
	
	List<Patient> findByphoneMobile(String phonenumber);

	void deleteById(int id);

	Page<Patient> findByUpdatedAtAfter(Instant since, Pageable paging);
	
	@Query(
		    value = """
		      select *
		      from patient p
		      where date_format(p.birthday, '%d/%m/%Y') like concat(:prefix, '%')
		    """,
		    countQuery = """
		      select count(*)
		      from patient p
		      where date_format(p.birthday, '%d/%m/%Y') like concat(:prefix, '%')
		    """,
		    nativeQuery = true
		  )
		  Page<Patient> searchByBirthdayPrefix(@Param("prefix") String prefix, Pageable pageable);
		
	
	@Query(value = """
			  select *
			  from patient p
			  where date_format(p.birthday, '%d/%m/%Y') like concat(:prefix, '%')
			""", nativeQuery = true)
			List<Patient> searchByBirthdayPrefix(@Param("prefix") String prefix);
}
