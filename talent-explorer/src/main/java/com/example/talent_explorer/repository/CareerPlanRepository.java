package com.example.talent_explorer.repository;

import com.example.talent_explorer.model.CareerPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerPlanRepository extends JpaRepository<CareerPlan, Long> {
    CareerPlan findByTaskId(String taskId);
}
