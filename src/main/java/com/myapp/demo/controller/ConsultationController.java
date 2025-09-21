package com.myapp.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.demo.Repository.IConsultationRepository;
import com.myapp.demo.entity.Consultation;
import com.myapp.demo.service.IConsultationService;

@RestController
@RequestMapping("api/consultation")
@CrossOrigin()
public class ConsultationController {
	
	@Autowired
	IConsultationRepository consultationRepo;
	
	@Autowired
	public IConsultationService consultationService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getPatientById() {
		
		try {
			List<Consultation> consultations = consultationService.getAllConsultations();
			
			Map<String, Object> response = new HashMap();
			response.put("consultations", consultations);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
