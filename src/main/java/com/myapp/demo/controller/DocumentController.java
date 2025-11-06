// com.myapp.demo.controller.DocumentController.java
package com.myapp.demo.controller;

import com.myapp.demo.entity.Document;
import com.myapp.demo.service.DocumentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.data.domain.Page;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/documents")
@CrossOrigin
@RequiredArgsConstructor
public class DocumentController {

	@Autowired
  DocumentService service;
	
	// === QR session & SSE en mémoire (remplace par Redis/DB si besoin) ===
	  private static final long QR_TTL_MS = 10 * 60 * 1000;
	  private final Map<String, Long> qrSessions = new ConcurrentHashMap<>();         // sid -> expiresAt
	  private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();  // sid -> emitter
	  private final Map<String, TempFileRef> tempFiles = new ConcurrentHashMap<>();   // tempId -> ref

	  private record TempFileRef(String tempId, String sid, String name, String mime, byte[] bytes) {}
	  private boolean expired(String sid) {
		    return sid == null || !qrSessions.containsKey(sid) || System.currentTimeMillis() > qrSessions.get(sid);
		  }

	 @GetMapping("/consultation")
  public ResponseEntity<?> listByConsultation(
      @RequestParam Long consultationId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Page<Document> p = service.listByConsultation(consultationId, page, size);
    return ResponseEntity.ok(
      java.util.Map.of(
        "totalItems", p.getTotalElements(),
        "totalPages", p.getTotalPages(),
        "currentPage", p.getNumber(),
        "documents", p.getContent()
      )
    );
  }

	 @GetMapping("/patient")
  public ResponseEntity<?> listByPatient(
      @RequestParam Long patientId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Page<Document> p = service.listByPatient(patientId, page, size);
    return ResponseEntity.ok(
      java.util.Map.of(
        "totalItems", p.getTotalElements(),
        "totalPages", p.getTotalPages(),
        "currentPage", p.getNumber(),
        "documents", p.getContent()
      )
    );
  }
	 
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> upload(
      @RequestParam Long patientId,
      @RequestParam Long consultationId,
      @RequestParam(required = false, defaultValue = "AUTRE") String type,
      @RequestParam(required = false) String createdBy,
      @RequestPart("file") MultipartFile file
  ) throws Exception {
    Document d = service.save(patientId,consultationId, type, createdBy, file);
    return ResponseEntity.ok(java.util.Map.of("document", d));
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<?> download(@PathVariable Long id) throws Exception {
    Document d = service.get(id);
    if (d == null) return ResponseEntity.notFound().build();

    var stream = service.openStream(d);
    var resource = new InputStreamResource(stream);

    String encoded = URLEncoder.encode(d.getOriginalName(), StandardCharsets.UTF_8);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(d.getMimeType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
        .contentLength(d.getSizeBytes())
        .body(resource);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
  
 

	  @PostMapping("/qr/session")
	  public Map<String, Object> createQrSession() {
	    String sid = java.util.UUID.randomUUID().toString();
	    long exp = System.currentTimeMillis() + QR_TTL_MS;
	    qrSessions.put(sid, exp);
	    // URL mobile relative -> ton front mobile (ex: /m/qr-upload?sid=...)
	    String mobilePath = "/m/qr-upload?sid=" + sid;
	    return Map.of("sessionId", sid, "mobileUrl", mobilePath, "expiresInSec", QR_TTL_MS / 1000);
	  }

	  @GetMapping(value = "/qr/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	  public SseEmitter stream(@RequestParam("sid") String sid) {
	    if (expired(sid)) throw new ResponseStatusException(HttpStatus.GONE, "Session expired");

	    long timeout = 10 * 60 * 1000; // 10 min
	    SseEmitter emitter = new SseEmitter(timeout);
	    sseEmitters.put(sid, emitter);
	    emitter.onCompletion(() -> sseEmitters.remove(sid));
	    emitter.onTimeout(() -> sseEmitters.remove(sid));

	    // ping initial (évite timeouts précoces)
	    try { emitter.send(SseEmitter.event().name("hello").data("ok")); } catch (Exception ignored) {}

	    var scheduler = Executors.newSingleThreadScheduledExecutor();
	    scheduler.scheduleAtFixedRate(() -> {
	      try { emitter.send(SseEmitter.event().comment("keep-alive")); }
	      catch (Exception e) { try { emitter.complete(); } catch (Exception ignore) {} scheduler.shutdownNow(); }
	    }, 25, 25, TimeUnit.SECONDS);

	    emitter.onCompletion(scheduler::shutdownNow);
	    emitter.onTimeout(scheduler::shutdownNow);

	    return emitter;
	  }


	  @PostMapping(value = "/qr/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	  public Map<String, Object> mobileUpload(
	      @RequestParam("sid") String sid,
	      @RequestPart("file") MultipartFile file
	  ) throws Exception {
	    if (expired(sid)) throw new ResponseStatusException(HttpStatus.GONE, "Session expired");

	    String tempId = java.util.UUID.randomUUID().toString();
	    TempFileRef ref = new TempFileRef(
	        tempId, sid,
	        file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo.jpg",
	        file.getContentType() != null ? file.getContentType() : "image/jpeg",
	        file.getBytes()
	    );
	    tempFiles.put(tempId, ref);

	    // notifier le desktop
	    SseEmitter em = sseEmitters.get(sid);
	    if (em != null) {
	      try {
	        em.send(SseEmitter.event().name("file-ready").data(Map.of(
	          "tempId", tempId,
	          "filename", ref.name(),
	          "mimeType", ref.mime()
	        )));
	      } catch (Exception ignore) {}
	    }
	    return Map.of("ok", true, "tempId", tempId);
	  }

	  @GetMapping("/qr/blob/{tempId}")
	  public ResponseEntity<byte[]> getTempBlob(@PathVariable String tempId) {
	    TempFileRef ref = tempFiles.get(tempId);
	    if (ref == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	    return ResponseEntity.ok()
	      .contentType(MediaType.parseMediaType(ref.mime()))
	      .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + ref.name() + "\"")
	      .body(ref.bytes());
	  }

		
}
