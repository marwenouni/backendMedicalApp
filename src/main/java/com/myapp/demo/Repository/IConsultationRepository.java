package com.myapp.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.Consultation;


public interface IConsultationRepository  extends JpaRepository<Consultation, Long> {
	
	List<Consultation> findAll();

}
