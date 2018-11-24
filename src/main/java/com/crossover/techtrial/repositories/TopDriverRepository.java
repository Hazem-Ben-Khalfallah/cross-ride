package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.TopDriver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TopDriverRepository extends CrudRepository<TopDriver, Long> {
    List<TopDriver> getTopRiders(@Param("count") Long count, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
