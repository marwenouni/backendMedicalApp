package com.myapp.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.demo.Repository.IConsultationRepository;
import com.myapp.demo.entity.Consultation;

@Service
public class ConsultationService implements IConsultationService {

	List<Consultation> consultations;
	@Autowired
	IConsultationRepository ConsultationRepo;

	public ConsultationService() {

	}

	public List<Consultation> getAllConsultations() {
		return ConsultationRepo.findAll();
	}

}
