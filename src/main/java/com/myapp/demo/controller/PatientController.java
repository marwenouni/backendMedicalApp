package com.myapp.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.demo.Repository.IPatientRepository;
import com.myapp.demo.entity.Patient;
import com.myapp.demo.service.IPatientService;

@RestController
@RequestMapping("api/patient")
@CrossOrigin()
public class PatientController {

	@Autowired
	IPatientRepository patientRepository;

	@Autowired
	public IPatientService patientService;

//	@GetMapping
//	List<Patient> getPatients() {
//		return patientService.getPatients();
//	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getPatientById(@RequestParam int id) {
		
		try {
			Patient Patient = patientService.getPatientById(id);;
			
			Map<String, Object> response = new HashMap();
			response.put("patients", Patient);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/filter")
	public ResponseEntity<Map<String, Object>> findAllPatientByFilter(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		try {
			// List<Patient> patients = new ArrayList()<Patient>();
			List<Patient> patients = new ArrayList<>(Arrays.asList());
			Pageable paging = PageRequest.of(page, size);

			Page<Patient> pagePatients = patientService.findAllPatientByFilter(paging);
			// findByFirstName("marwen", paging);
			patients = pagePatients.getContent();
			System.out.println("patients" + patients);

			Map<String, Object> response = new HashMap();
			response.put("patients", patients);
			response.put("currentPage", pagePatients.getNumber());
			response.put("totalItems", pagePatients.getTotalElements());
			response.put("totalPages", pagePatients.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping("/searchbyfirstname")
	public ResponseEntity<Map<String, Object>> findAllPatientByFirstName(String firstname) {
		try {
			// List<Patient> patients = new ArrayList()<Patient>();
			List<Patient> patients = new ArrayList<>(Arrays.asList());

			List<Patient> pagePatients = patientService.findAllPatientByFirstName(firstname);
			patients = pagePatients;
			System.out.println("patients" + patients);

			Map<String, Object> response = new HashMap();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/searchbylastname")
	public ResponseEntity<Map<String, Object>> findAllPatientByLastName(String lastname) {
		try {
			// List<Patient> patients = new ArrayList()<Patient>();
			List<Patient> patients = new ArrayList<>(Arrays.asList());

			List<Patient> pagePatients = patientService.findAllPatientByLastName(lastname);
			patients = pagePatients;
			System.out.println("patients" + patients);

			Map<String, Object> response = new HashMap();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/searchbybirthday")
	public ResponseEntity<Map<String, Object>> findAllPatientByBirthday(String birthday) {
		try {
			// List<Patient> patients = new ArrayList()<Patient>();
			List<Patient> patients = new ArrayList<>(Arrays.asList());

			List<Patient> pagePatients = patientService.findAllPatientByBirthday(birthday);
			patients = pagePatients;
			System.out.println("patients" + patients);

			Map<String, Object> response = new HashMap();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/searchbyphonenumber")
	public ResponseEntity<Map<String, Object>> findAllPatientByPhoneNumber(String phonenumber) {
		try {
			// List<Patient> patients = new ArrayList()<Patient>();
			List<Patient> patients = new ArrayList<>(Arrays.asList());

			List<Patient> pagePatients = patientService.findAllPatientByPhoneNumber(phonenumber);
			patients = pagePatients;
			System.out.println("patients" + patients);

			Map<String, Object> response = new HashMap();
			response.put("patients", patients);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping("/serachfilter")
	  public ResponseEntity<Map<String, Object>> findByStartingWith(
			String firstName,
			String birthday,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "3") int size,
	        String sort, 
	        String filter
	        
	      ) {
	    try {
	    	
	     // List<Patient> patients = new ArrayList()<Patient>();
	      List<Patient> patients = new ArrayList<>(Arrays.asList());
	      Page<Patient> pagePatients=null;
	      Pageable paging = PageRequest.of(page, size,getSort(sort, filter));
	      String fName="";
	      String lName="";
	      String[] parts = firstName.split(" ", 2);

	        if (parts.length == 2) 
	        {
	             fName = parts[0];
	             lName = parts[1];
	        }
	        else 
	        {
	        	fName=firstName;
	        }
	      if((firstName.length()>0 ) && !(birthday.length() > 0)  )
	      {
	    	 // pagePatients = patientService.getPatientsByFirstNameAndLastNameContaining(firstName,paging);
	    	  pagePatients = patientService.getPatientsByFirstNameAndLastNameContaining(fName, lName,paging,sort,filter);
	      }
	      
	      else if(! (firstName.length()>0 ) && (birthday.length() > 0)  )
	      {
	    	  pagePatients = patientService.findByBirthdayStartingWith(birthday,paging);
	  	    
	      }
	      else
	      {
	    	  pagePatients = patientService.findAllPatientByFilter(paging);
	      }
	      
	      patients = pagePatients.getContent();
	      System.out.println("patients"+patients);
	            
	      Map<String, Object> response = new HashMap();
	      response.put("patients", patients);
	      response.put("currentPage", pagePatients.getNumber());
	      response.put("totalItems", pagePatients.getTotalElements());
	      response.put("totalPages", pagePatients.getTotalPages());
	      
	      return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	        catch (Exception e) {
	    	System.out.println(e.getMessage());
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	Sort getSort(String sort, String filter) {
		if (sort.equals("ASC"))
			return Sort.by(Sort.Direction.ASC, filter);
		else 
			return Sort.by(Sort.Direction.DESC, filter);
		

	}
	
	
	@PostMapping("/add-patient")
	  public ResponseEntity<Map<String, Object>> addPatient(@RequestBody 
			  Patient patient) {
		//birthday="birthday";
		//Patient patient=new Patient(idCabinet,firstname,lastname,birthday,numberPhone,email,city);
	    System.out.println("birthday is : "+patient.getBirthday());
	    
		try {
	    patientService.add(patient);
	    		Map<String, Object> response = new HashMap();
	      response.put("patients", patient);
	      
	      return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	        catch (Exception e) {
	    	System.out.println(e.getMessage());
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	


}
