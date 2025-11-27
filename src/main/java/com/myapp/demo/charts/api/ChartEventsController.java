package com.myapp.demo.charts.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class ChartEventsController {

  private final List<SseEmitter> clients = new CopyOnWriteArrayList<>();

  @GetMapping(path = "/charts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter stream() {
    var e = new SseEmitter(0L);
    clients.add(e);
    e.onCompletion(() -> clients.remove(e));
    e.onTimeout(()    -> clients.remove(e));
    e.onError(_e  -> clients.remove(e));
    try { e.send(SseEmitter.event().name("ping").data("ok:" + Instant.now())); }
    catch (IOException ignore) {}
    return e;
  }

  /** Appelé après create/update/delete chart */
  public void notifyChartsChanged() {
    var ts = Instant.now().toEpochMilli();
    for (var e : List.copyOf(clients)) {
      try { e.send(SseEmitter.event().name("charts-changed").data(ts)); }
      catch (Exception ex) { clients.remove(e); }
    }
  }
}
