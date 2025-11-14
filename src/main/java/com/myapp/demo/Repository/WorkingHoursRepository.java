package com.myapp.demo.Repository;


import com.myapp.demo.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
  List<WorkingHours> findByCabinetId(Long cabinetId);
}

