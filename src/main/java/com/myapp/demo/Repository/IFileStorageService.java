package com.myapp.demo.Repository;

import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Path;

public interface IFileStorageService {
	  public void init();

	  public void save(MultipartFile file);

	  public Resource load(String filename);

	  public void deleteAll();

	}
