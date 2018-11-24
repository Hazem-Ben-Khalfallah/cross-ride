/**
 *
 */
package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Ride;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.Instant;

/**
 * @author crossover
 */
@RestResource(exported = false)
public interface RideRepository extends CrudRepository<Ride, Long> {
    /**
     * @should return true if the person is a rider in another ride
     * @should return true if the person is a driver in another ride
     * @should return true if the person in another ride in the same time range
     * @should return false if the person is a rider in another ride but in another time range
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM ride r " +
            " WHERE (r.rider_id = :id OR r.driver_id = :id ) " +
            "  AND  (:startTime BETWEEN r.start_time AND r.end_time " +
            "         OR :endTime BETWEEN r.start_time AND r.end_time" +
            "         OR (:startTime <= r.start_time  AND :endTime >= r.end_time))", nativeQuery = true)
    boolean checkIsInAnotherRide(@Param("id") Long id, @Param("startTime") Instant startTime, @Param("endTime") Instant endTime);
}
