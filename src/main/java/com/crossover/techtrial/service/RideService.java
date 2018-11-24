/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * RideService for rides.
 *
 * @author crossover
 */
public interface RideService {

    /**
     * @should throw an exception if riderId is null
     * @should throw an exception if driverId is null
     * @should throw an exception if driver is the same as the rider
     * @should throw an exception if endTime is before startTime
     * @should throw an exception if rider is not found
     * @should throw an exception if driver is not found
     * @should throw an exception if the rider is in another ride in the same time range
     */
    Ride save(Ride ride);

    Optional<Ride> findById(Long rideId);

    List<TopDriverDTO> getTopDrivers(Long count, LocalDateTime startTime, LocalDateTime endTime);
}
