// com.myapp.demo.Repository.IDocumentRepository.java
package com.myapp.demo.Repository;

import com.myapp.demo.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IDocumentRepository extends JpaRepository<Document, Long> {
  Page<Document> findByIdChartOrderByUpdatedAtDesc(Long idChart, Pageable pageable);

Page<Document> findByIdConsultationOrderByUpdatedAtDesc(Long idConsultation, PageRequest of);
}
