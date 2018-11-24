package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.utils.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;

/**
 * @author hazem
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RideRepository rideRepository;

    private Instant now;
    private Ride ride;

    @Before
    public void setUp() {
        final Person driver = personRepository.save(Person.newBuilder()
                .setName("SpeedyMan")
                .setEmail("driver@mail.com")
                .build());

        final Person rider = personRepository.save(Person.newBuilder()
                .setName("Mr Harry Up")
                .setEmail("rider@mail.com")
                .build());

        now = DateUtils.now();

        ride = rideRepository.save(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(60L) //
                .setStartTime(now) //
                .setEndTime(DateUtils.addHours(now, 1)) //
                .build());
    }

    /**
     * @verifies return true if the person is a rider in another ride
     * @see RideRepository#checkIsInAnotherRide(Long, java.time.Instant, java.time.Instant)
     */
    @Test
    public void checkIsInAnotherRide_shouldReturnTrueIfThePersonIsARiderInAnotherRide() throws Exception {
        // when
        final boolean inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), DateUtils.addMinutes(now, 10), DateUtils.addMinutes(now, 30));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();
    }

    /**
     * @verifies return true if the person is a driver in another ride
     * @see RideRepository#checkIsInAnotherRide(Long, java.time.Instant, java.time.Instant)
     */
    @Test
    public void checkIsInAnotherRide_shouldReturnTrueIfThePersonIsADriverInAnotherRide() throws Exception {
        // when
        final boolean inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getDriver().getId(), DateUtils.addMinutes(now, 10), DateUtils.addMinutes(now, 30));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();
    }

    /**
     * @verifies return true if the person in another ride in the same time range
     * @see RideRepository#checkIsInAnotherRide(Long, Instant, Instant)
     */
    @Test
    public void checkIsInAnotherRide_shouldReturnTrueIfThePersonInAnotherRideInTheSameTimeRange() throws Exception {
        // when
        boolean inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), now, DateUtils.addHours(now, 1));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();

        // when
        inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), now, DateUtils.addHours(now, 3));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();

        // when
        inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), DateUtils.addHours(now, -1), DateUtils.addMinutes(now, 30));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();

        // when
        inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), DateUtils.addMinutes(now, 15), DateUtils.addMinutes(now, 30));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();

        // when
        inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), DateUtils.addHours(now, -1), DateUtils.addHours(now, 2));
        // then
        Assertions.assertThat(inAnotherRide).isTrue();
    }

    /**
     * @verifies return false if the person is a rider in another ride but in another time range
     * @see RideRepository#checkIsInAnotherRide(Long, java.time.Instant, java.time.Instant)
     */
    @Test
    public void checkIsInAnotherRide_shouldReturnFalseIfThePersonIsARiderInAnotherRideButInAnotherTimeRange() throws Exception {
        // when
        boolean inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), DateUtils.addHours(now, 2), DateUtils.addHours(now, 3));
        // then
        Assertions.assertThat(inAnotherRide).isFalse();

        // when
        inAnotherRide = rideRepository.checkIsInAnotherRide(ride.getRider().getId(), DateUtils.addHours(now, -3), DateUtils.addHours(now, -2));
        // then
        Assertions.assertThat(inAnotherRide).isFalse();
    }
}