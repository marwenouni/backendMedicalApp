package com.myapp.demo.charts.infra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.myapp.demo.charts.domain.ChartIdentity;

public interface ChartIdentityRepository extends JpaRepository<ChartIdentity, Long> {

    @Query("""
        SELECT i FROM ChartIdentity i
        WHERE (:q IS NULL OR :q = '' 
               OR LOWER(i.firstName) LIKE LOWER(CONCAT(:q,'%'))
               OR LOWER(i.lastName)  LIKE LOWER(CONCAT(:q,'%')))
    """)
    Page<ChartIdentity> searchByName(@Param("q") String q, Pageable pageable);

    List<ChartIdentity> findByFirstNameStartingWith(String firstname);

	List<ChartIdentity> findByFirstNameContaining(String firstname);

	List<ChartIdentity>  findByLastNameContaining(String lastname);

	List<ChartIdentity> findByBirthday(LocalDate birthday);

	List<ChartIdentity> findByPhoneMobile(String phone);

	// Pour la pagination
	  @Query(
	      value = """
	        select *
	        from chart_identity pi
	        where date_format(pi.birthday, '%d/%m/%Y') like concat(:prefix, '%')
	      """,
	      countQuery = """
	        select count(*)
	        from chart_identity pi
	        where date_format(pi.birthday, '%d/%m/%Y') like concat(:prefix, '%')
	      """,
	      nativeQuery = true
	  )
	  Page<ChartIdentity> searchByBirthdayPrefix(@Param("prefix") String prefix, Pageable pageable);

	  // Variante sans pagination
	  @Query(value = """
	        select *
	        from chart_identity pi
	        where date_format(pi.birthday, '%d/%m/%Y') like concat(:prefix, '%')
	      """,
	      nativeQuery = true
	  )
	  List<ChartIdentity> searchByBirthdayPrefix(@Param("prefix") String prefix);
}

