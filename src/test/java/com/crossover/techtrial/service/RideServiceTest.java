package com.crossover.techtrial.service;

import com.crossover.techtrial.exceptions.CustomErrorCode;
import com.crossover.techtrial.exceptions.CustomException;
import com.crossover.techtrial.exceptions.CustomMatcher;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.RideRepository;
import com.crossover.techtrial.utils.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @author hazem
 */
@RunWith(MockitoJUnitRunner.class)
public class RideServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private RideRepository rideRepositoryMock;
    @Mock
    private PersonService personServiceMock;
    @Spy
    @InjectMocks
    private RideServiceImpl rideServiceSpy;

    /**
     * @verifies throw an exception if riderId is null
     * @see RideService#save(Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfRiderIdIsNull() throws Exception {
        // given
        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.BAD_ARGS));
        thrown.expectMessage("riderId cannot be null");

        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(1L)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(null)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();

        // when
        rideServiceSpy.save(newRide);
    }

    /**
     * @verifies throw an exception if driverId is null
     * @see RideService#save(Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfDriverIdIsNull() throws Exception {
        // given
        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.BAD_ARGS));
        thrown.expectMessage("driverId cannot be null");

        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(null)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(1L)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();

        // when
        rideServiceSpy.save(newRide);
    }

    /**
     * @verifies throw an exception if driver is the same as the rider
     * @see RideService#save(Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfDriverIsTheSameAsTheRider() throws Exception {
        // given
        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.BAD_ARGS));
        thrown.expectMessage("The driver can not be also the rider");

        final Long driverId = 1L;
        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(driverId)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(driverId)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();

        // when
        rideServiceSpy.save(newRide);
    }


    /**
     * @verifies throw an exception if endTime is before startTime
     * @see RideService#save(com.crossover.techtrial.model.Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfEndTimeIsBeforeStartTime() throws Exception {
        // given
        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.BAD_ARGS));
        thrown.expectMessage("EndTime should be after startTime");

        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(1L)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(2L)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), -1)) //
                .build();
        // when
        rideServiceSpy.save(newRide);
    }

    /**
     * @verifies throw an exception if rider is not found
     * @see RideService#save(Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfRiderIsNotFound() throws Exception {
        // given
        final Long riderId = 2L;
        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.PERMISSION_DENIED));
        thrown.expectMessage(String.format("The rider [id: %s] is not registered", riderId));

        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(1L)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(riderId)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();

        when(personServiceMock.existsById(riderId)).thenReturn(false);
        // when
        rideServiceSpy.save(newRide);
    }

    /**
     * @verifies throw an exception if driver is not found
     * @see RideService#save(Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfDriverIsNotFound() throws Exception {
        // given
        final Long driverId = 1L;

        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.PERMISSION_DENIED));
        thrown.expectMessage(String.format("The driver [id: %s] is not registered", driverId));

        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(driverId)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(2L)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();

        when(personServiceMock.existsById(newRide.getRider().getId())).thenReturn(true);
        when(personServiceMock.existsById(driverId)).thenReturn(false);
        // when
        rideServiceSpy.save(newRide);
    }

    /**
     * @verifies throw an exception if the rider is in another ride in the same time range
     * @see RideService#save(Ride)
     */
    @Test
    public void save_shouldThrowAnExceptionIfTheRiderIsInAnotherRideInTheSameTimeRange() throws Exception {
        // given
        thrown.expect(CustomException.class);
        thrown.expect(CustomMatcher.hasCode(CustomErrorCode.BAD_ARGS));
        thrown.expectMessage("The rider cannot be in another ride");

        final Long riderId = 2L;
        final Ride newRide = Ride.newBuilder() //
                .setDriver(Person.newBuilder()
                        .setId(1L)
                        .build()) //
                .setRider(Person.newBuilder()
                        .setId(riderId)
                        .build()) //
                .setDistance(90L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();

        when(personServiceMock.existsById(newRide.getRider().getId())).thenReturn(true);
        when(personServiceMock.existsById(newRide.getDriver().getId())).thenReturn(true);

        when(rideRepositoryMock.checkIsInAnotherRide(newRide.getRider().getId(), newRide.getStartTime(), newRide.getEndTime()))
                .thenReturn(true);
        // when
        rideServiceSpy.save(newRide);
    }

}