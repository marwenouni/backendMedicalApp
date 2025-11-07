package com.myapp.demo.service;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myapp.demo.Repository.IFileStorageService;

import jakarta.annotation.Resource;

@Service
public class FileStorageService implements IFileStorageService{

	private final Path root = Paths.get("uploads");
	
	@Override
	public void init() {
		try {
		      Files.createDirectories(root);
		    } catch (IOException e) {
		      throw new RuntimeException("Could not initialize folder for upload!");
		    }
		
	}

	@Override
	public void save(MultipartFile file) {
		try {
		      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		    } catch (Exception e) {
		      if (e instanceof FileAlreadyExistsException) {
		        throw new RuntimeException("A file of that name already exists.");
		      }

		      throw new RuntimeException(e.getMessage());
		    }
		
	}

	   /** 🔹 Lis un fichier existant et retourne son contenu en bytes */
    public byte[] read(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) {
            throw new IllegalArgumentException("Le chemin du fichier est vide");
        }

        Path file = root.resolve(relativePath).normalize();
        if (!Files.exists(file)) {
            throw new IOException("Fichier introuvable: " + file);
        }

        return Files.readAllBytes(file);
    }

    /** Supprime un fichier du stockage */
    public void delete(String relativePath) throws IOException {
        if (relativePath == null || relativePath.isBlank()) return;
        Path file = root.resolve(relativePath).normalize();
        if (Files.exists(file)) Files.delete(file);
    }

	@Override
	public Resource load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	

}
