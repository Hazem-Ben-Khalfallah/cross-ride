/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.exceptions.CustomErrorCode;
import com.crossover.techtrial.exceptions.CustomException;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;
import com.crossover.techtrial.repositories.TopDriverRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author crossover
 */
@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final PersonService personService;
    private final TopDriverRepository topDriverRepository;

    public RideServiceImpl(RideRepository rideRepository, PersonService personService,
                           TopDriverRepository topDriverRepository) {
        this.rideRepository = rideRepository;
        this.personService = personService;
        this.topDriverRepository = topDriverRepository;
    }


    public Ride save(Ride ride) {
        if (ride.getRider() == null || ride.getRider().getId() == null) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "riderId cannot be null");
        }

        if (ride.getDriver() == null || ride.getDriver().getId() == null) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "driverId cannot be null");
        }

        if (ride.getRider().getId().equals(ride.getDriver().getId())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "The driver can not be also the rider");
        }

        if (ride.getEndTime().isBefore(ride.getStartTime())) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "EndTime should be after startTime");
        }

        final boolean riderExists = personService.existsById(ride.getRider().getId());
        if (!riderExists) {
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "The rider [id: %s] is not registered", ride.getRider().getId().toString());
        }

        final boolean driverExists = personService.existsById(ride.getDriver().getId());
        if (!driverExists) {
            throw new CustomException(CustomErrorCode.PERMISSION_DENIED, "The driver [id: %s] is not registered", ride.getDriver().getId().toString());
        }

        final boolean isInAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), ride.getStartTime(), ride.getEndTime());
        if (isInAnotherRide) {
            throw new CustomException(CustomErrorCode.BAD_ARGS, "The rider cannot be in another ride");
        }

        return rideRepository.save(ride);
    }

    public Optional<Ride> findById(Long rideId) {
        return rideRepository.findById(rideId);
    }

    @Override
    public List<TopDriverDTO> getTopDrivers(Long count, LocalDateTime startTime, LocalDateTime endTime) {
        return topDriverRepository.getTopRiders(count, startTime, endTime)
                .stream()
                .map(topDriver -> new TopDriverDTO(topDriver.getName(), topDriver.getEmail(), //
                        topDriver.getTotalRideDurationInSeconds(), topDriver.getMaxRideDurationInSeconds(),
                        topDriver.getAverageDistance()))
                .collect(Collectors.toList());
    }

}
