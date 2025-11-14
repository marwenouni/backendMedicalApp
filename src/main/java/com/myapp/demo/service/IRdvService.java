package com.myapp.demo.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.demo.dto.BookDTO;
import com.myapp.demo.dto.MoveDTO;
import com.myapp.demo.dto.RdvDTO;
import com.myapp.demo.dto.SlotDTO;
import com.myapp.demo.entity.RendezVous;

public interface IRdvService {

	// ====== SLOTS ======
	List<SlotDTO> computeSlots(Long cabinetId, String dateFrom, String dateTo, String tz);

	// ====== BOOK ======
	RdvDTO book(BookDTO dto);

	// ====== CANCEL ======
	RdvDTO cancel(Long id, String reason);

	// ====== MOVE ======
	RdvDTO move(Long id, MoveDTO dto);

	// ====== LIST ======
	List<RdvDTO> list(Long cabinetId, String dateFrom, String dateTo);

	Page findUpdatedSince(Instant since, Pageable pageable);

	Page<RendezVous> findByUpdatedAtAfter(Instant since, Pageable paging);

}