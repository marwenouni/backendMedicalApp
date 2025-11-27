package com.myapp.demo.charts.app.interfaces;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myapp.demo.charts.api.dto.ChartDto;

public interface IChartService {
	

	List<ChartDto> getCharts();

	Page<ChartDto> getChartsStartingWith(String firstName,Pageable paging);

	Page<ChartDto> getChartsByFirstNameContaining(String firstName,Pageable paging);
	
    Page<ChartDto> findAllChartByFilter(Pageable pageable);
	
	
	/**
	 * mixte search between firstname and lastname.
	 *
	 * @param chaine     de caractére
	 * @param pagination
	 * @return list of charts
	 */
	Page<ChartDto> getChartsByFirstNameAndLastNameContaining(String firstname, String lastname, Pageable pageable,
			String sort, String filter);

	List<ChartDto> findAllChartByIdCabinet(Long id);
	

	List<ChartDto> findAllChartByIdProvider(Long idProvider);

	Optional<ChartDto> getChartById(Long id);

	List<ChartDto> findAllChartByFirstName(String firstname);
	
	List<ChartDto> findAllChartByLastName(String firstname);
	
	List<ChartDto> findAllChartByPhoneMobile(String phonenumber);

	void delete(int id);

	Optional<ChartDto> findByClientUuid(String uuid);

	List<ChartDto> findUpdated(Instant since);


	List<ChartDto> findAllChartByBirthday(LocalDate birthday);

	ChartDto add(ChartDto dto);

	ChartDto createIdempotent(ChartDto dto);

	ChartDto update(ChartDto dto);


	




		
}
