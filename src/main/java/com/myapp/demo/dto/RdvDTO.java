package com.myapp.demo.dto;

import com.myapp.demo.entity.RendezVous;
import java.time.Instant;

public record RdvDTO(
    Long id, Long cabinetId, Long chartId,
    Instant startAt, Instant endAt,
    String status, String motif,
    Long movedFromId,
    Instant createdAt, Instant updatedAt
) {
  public static RdvDTO of(RendezVous r){
    return new RdvDTO(
      r.getId(), r.getCabinetId(), r.getChartId(),
      r.getStartAt(), r.getEndAt(),
      r.getStatus().name(), r.getMotif(),
      r.getMovedFromId(),
      r.getCreatedAt(), r.getUpdatedAt()
    );
  }
}

