// com.myapp.demo.service.DocumentService.java
package com.myapp.demo.service;

import com.myapp.demo.Repository.IDocumentRepository;
import com.myapp.demo.entity.Document;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class DocumentService {

	@Autowired
    IDocumentRepository repo;


  @Value("${app.storage.root:/tmp/uploads}") // dossier racine configurable
  private String storageRoot;

  public Page<Document> listByPatient(Long idPatient, int page, int size) {
    return repo.findByIdPatientOrderByUpdatedAtDesc(idPatient, PageRequest.of(page, size));
  }
  
  public Page<Document> listByConsultation(Long IdConsultation, int page, int size) {
	  return repo.findByIdConsultationOrderByUpdatedAtDesc(IdConsultation, PageRequest.of(page, size));

	}

  public Document get(Long id) {
    return repo.findById(id).orElse(null);
  }

  @Transactional
  public Document save(Long idPatient,Long idConsultation,String type, String createdBy, MultipartFile file) throws Exception {
    // 1) checksum
    String sha256 = sha256Hex(file.getInputStream());

    // 2) dossier date-based
    LocalDate today = LocalDate.now();
    Path dir = Path.of(storageRoot, String.valueOf(today.getYear()),
                       String.format("%02d", today.getMonthValue()),
                       String.format("%02d", today.getDayOfMonth()));
    Files.createDirectories(dir);

    // 3) nom de fichier unique (évite collisions)
    String ext = guessExt(file.getOriginalFilename());
    String storedName = System.currentTimeMillis() + "-" + Math.abs(sha256.hashCode()) + (ext.isEmpty()? "" : "."+ext);
    Path target = dir.resolve(storedName);

    // 4) (option) Antivirus ici
    // scanWithClamAV(file.getInputStream());

    // 5) copie disque
    try (InputStream in = file.getInputStream()) {
      Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
    }

    // 6) persistance meta
    Document d = new Document();
    d.setIdPatient(idPatient);
    d.setIdConsultation(idConsultation);
    d.setType(type != null ? type : "AUTRE");
    d.setOriginalName(file.getOriginalFilename());
    d.setStoredName(storedName);
    d.setMimeType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
    d.setSizeBytes(file.getSize());
    d.setChecksumSha256(sha256);
    d.setStoragePath(target.toAbsolutePath().toString());
    d.setCreatedBy(createdBy);
    return repo.save(d);
  }

  public InputStream openStream(Document d) throws IOException {
    return Files.newInputStream(Path.of(d.getStoragePath()), StandardOpenOption.READ);
  }

  @Transactional
  public void delete(Long id) throws IOException {
    Document d = get(id);
    if (d == null) return;
    try {
      Files.deleteIfExists(Path.of(d.getStoragePath()));
    } finally {
      repo.deleteById(id);
    }
  }

  // ---------- helpers ----------
  private static String sha256Hex(InputStream in) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] buf = new byte[8192];
    int r;
    while ((r = in.read(buf)) != -1) md.update(buf, 0, r);
    return HexFormat.of().formatHex(md.digest());
  }

  private static String guessExt(String name) {
    if (name == null) return "";
    int i = name.lastIndexOf('.');
    return i > 0 && i < name.length() - 1 ? name.substring(i + 1).toLowerCase() : "";
  }


}
