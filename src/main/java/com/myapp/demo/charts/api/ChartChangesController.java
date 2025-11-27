package com.myapp.demo.charts.api;

import com.myapp.demo.Repository.IChartRepository;
import com.myapp.demo.charts.api.dto.ChartDto;
import com.myapp.demo.charts.domain.ChartChange;
import com.myapp.demo.charts.infra.repository.ChartChangeRepository;
import com.myapp.demo.entity.Chart;
import com.myapp.demo.charts.api.dto.ChartDelta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/chart")
@CrossOrigin
public class ChartChangesController {

  private final ChartChangeRepository changesRepo;
  private final IChartRepository chartRepo;

  public ChartChangesController(ChartChangeRepository changesRepo, IChartRepository chartRepo) {
    this.changesRepo = changesRepo;
    this.chartRepo = chartRepo;
  }

//  @GetMapping("/changes")
//  public ChartDelta changes(@RequestParam("since") long since,
//                              @RequestParam(value = "limit", defaultValue = "500") int limit) {
//
//	  var pageReq = PageRequest.of(0, limit, Sort.by("changeId").ascending());
//	  var page    = changesRepo.findByChangeIdGreaterThanOrderByChangeIdAsc(since, pageReq);
//	  var changes = page.getContent();
//	  boolean more = page.hasNext();
//
//    var idsUpsert = changes.stream()
//        .filter(c -> !"D".equalsIgnoreCase(c.getOp()))
//        .map(ChartChange::getRowId)
//        .distinct()
//        .toList();
//
//    var idsDelete = changes.stream()
//        .filter(c ->  "D".equalsIgnoreCase(c.getOp()))
//        .map(ChartChange::getRowId)
//        .distinct()
//        .toList();
//    
//    
//
//    // le PK de Chart est Integer ? Alors caste :
//    // var upsertIdsInt = idsUpsert.stream().map(Long::intValue).toList();
//    // var charts = upsertIdsInt.isEmpty() ? List.<Chart>of() : chartRepo.findAllById(upsertIdsInt);
//
//    // si ton PK Chart est Long, garde Long :
//    var charts = idsUpsert.isEmpty() ? List.<Chart>of() : chartRepo.findAllById(idsUpsert);
//
//    var upserts = charts.stream().map(ChartDto::from).toList();
//
//    long next    = changes.isEmpty() ? since : changes.get(changes.size() - 1).getChangeId();
//     more = changes.size() == limit;
//     
//    return new ChartDelta(since, next, upserts, idsDelete, more);
//  }
}
