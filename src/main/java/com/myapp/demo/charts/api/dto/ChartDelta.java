package com.myapp.demo.charts.api.dto;

import java.util.List;

public record ChartDelta(
  long since,
  long next,
  List<ChartDto> upserts,
  List<Long> deletes,
  boolean hasMore
) {}
