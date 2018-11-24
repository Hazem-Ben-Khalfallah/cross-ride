/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.service.RideService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * RideController for Ride related APIs.
 *
 * @author crossover
 */
@RestController
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * @should create new ride
     */
    @PostMapping(path = "/api/ride")
    public ResponseEntity<Ride> createNewRide(@RequestBody Ride ride) {
        return ResponseEntity.ok(rideService.save(ride));
    }

    /**
     * @should get a ride by id
     * @should return a not found response
     */
    @GetMapping(path = "/api/ride/{ride-id}")
    public ResponseEntity<Ride> getRideById(@PathVariable(name = "ride-id") Long rideId) {
        final Optional<Ride> ride = rideService.findById(rideId);
        return ride.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * This API returns the top 5 drivers with their email,name, total minutes, maximum ride duration in minutes.
     * Only rides that starts and ends within the mentioned durations should be counted.
     * Any rides where either start or endtime is outside the search, should not be considered.
     * <p>
     * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
     *
     * @should return top drivers in time range
     * @should return top drivers with valid values
     * @should return top drivers count
     */
    @GetMapping(path = "/api/top-drivers")
    public ResponseEntity<List<TopDriverDTO>> getTopDriver(
            @RequestParam(value = "max", defaultValue = "5") Long count,
            @RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        final List<TopDriverDTO> topDrivers = rideService.getTopDrivers(count, startTime, endTime);
        return ResponseEntity.ok(topDrivers);

    }

}
