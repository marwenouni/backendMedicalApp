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

import com.myapp.demo.entity.Chart;

public interface IChartRepository extends JpaRepository<Chart, Long> {
	
	Chart findByEmail(String email);

	List<Chart> findAll();
	
   Optional<Chart>	 findById(long id);
	
	List<Chart> findByFirstName(String firstname);

	
	List<Chart> findByLastNameStartingWith(String firstname);

	Optional<Chart> findByClientUuid(String clientUuid);
	
	List<Chart> getChartsByFirstNameContaining(String firstname);
	
	List<Chart> getChartsByLastNameContaining(String firstname);

	//Page<Chart> findByBirthdayStartingWith(String birthday,Pageable page);

	List<Chart> findByFirstNameAndLastName(String firstname, String firstname2);

	List<Chart> findByFirstNameContainingAndLastNameContaining(String string, String string2);

	List<Chart> findByLastName(String firstname);

	List<Chart> findByFirstNameContainingAndLastName(String firstname, String lastname);

	List<Chart> findByFirstNameContaining(String lastname);

	List<Chart> findByLastNameContaining(String lastname);

	List<Chart> findByFirstNameStartingWith(String firstname);
	
	Chart save(Chart chart);

	Chart getById(int i);

	List<Chart> findByBirthday(LocalDate birthday);
	
	List<Chart> findByphoneMobile(String phonenumber);

	void deleteById(int id);

	Page<Chart> findByUpdatedAtAfter(Instant since, Pageable paging);
	
	@Query(
		    value = """
		      select *
		      from chart p
		      where date_format(p.birthday, '%d/%m/%Y') like concat(:prefix, '%')
		    """,
		    countQuery = """
		      select count(*)
		      from chart p
		      where date_format(p.birthday, '%d/%m/%Y') like concat(:prefix, '%')
		    """,
		    nativeQuery = true
		  )
		  Page<Chart> searchByBirthdayPrefix(@Param("prefix") String prefix, Pageable pageable);
		
	
	@Query(value = """
			  select *
			  from chart p
			  where date_format(p.birthday, '%d/%m/%Y') like concat(:prefix, '%')
			""", nativeQuery = true)
			List<Chart> searchByBirthdayPrefix(@Param("prefix") String prefix);
}
