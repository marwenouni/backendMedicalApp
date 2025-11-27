package com.myapp.demo.Repository.patient;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import com.myapp.demo.dto.PatientDto;
import com.myapp.demo.entity.Patient;
import com.myapp.demo.entity.patient.PatientIdentity;

public interface PatientIdentityRepository extends JpaRepository<PatientIdentity, Long> {

    @Query("""
        SELECT i FROM PatientIdentity i
        WHERE (:q IS NULL OR :q = '' 
               OR LOWER(i.firstName) LIKE LOWER(CONCAT(:q,'%'))
               OR LOWER(i.lastName)  LIKE LOWER(CONCAT(:q,'%')))
    """)
    Page<PatientIdentity> searchByName(@Param("q") String q, Pageable pageable);

    List<PatientIdentity> findByFirstNameStartingWith(String firstname);

	List<PatientIdentity> findByFirstNameContaining(String firstname);

	List<PatientIdentity>  findByLastNameContaining(String lastname);

	List<PatientIdentity> findByBirthday(LocalDate birthday);

	List<PatientIdentity> findByPhoneMobile(String phone);

	// Pour la pagination
	  @Query(
	      value = """
	        select *
	        from patient_identity pi
	        where date_format(pi.birthday, '%d/%m/%Y') like concat(:prefix, '%')
	      """,
	      countQuery = """
	        select count(*)
	        from patient_identity pi
	        where date_format(pi.birthday, '%d/%m/%Y') like concat(:prefix, '%')
	      """,
	      nativeQuery = true
	  )
	  Page<PatientIdentity> searchByBirthdayPrefix(@Param("prefix") String prefix, Pageable pageable);

	  // Variante sans pagination
	  @Query(value = """
	        select *
	        from patient_identity pi
	        where date_format(pi.birthday, '%d/%m/%Y') like concat(:prefix, '%')
	      """,
	      nativeQuery = true
	  )
	  List<PatientIdentity> searchByBirthdayPrefix(@Param("prefix") String prefix);
}

