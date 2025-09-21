package com.myapp.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.myapp.demo.dto.ResponseMessage;
import com.myapp.demo.service.FileStorageService;
import com.myapp.demo.service.TestViewerService;

@Controller
public class FilesController {
	
	@Autowired
	  FileStorageService storageService;
	
	@Autowired
	  TestViewerService testViewer;
	
	@PostMapping("/upload")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";
	    try {
	      storageService.save(file);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }
	
	@GetMapping("/diff")
	  public ResponseEntity<String> diff() throws Exception {
	     return ResponseEntity.ok(testViewer.getDiffernce());
		   
	  }

}
