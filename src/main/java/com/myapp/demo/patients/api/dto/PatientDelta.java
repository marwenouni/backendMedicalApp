package com.myapp.demo.patients.api.dto;

import java.util.List;

public record PatientDelta(
  long since,
  long next,
  List<PatientDto> upserts,
  List<Long> deletes,
  boolean hasMore
) {}
