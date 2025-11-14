package com.myapp.demo.dto;

import java.time.Instant;

public record SlotDTO(String date, Instant startAt, Instant endAt, boolean available) {}
