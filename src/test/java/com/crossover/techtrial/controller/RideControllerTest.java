/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopDriverDTO;
import com.crossover.techtrial.model.Person;
import com.crossover.techtrial.model.Ride;
import com.crossover.techtrial.repositories.PersonRepository;
import com.crossover.techtrial.repositories.RideRepository;
import com.crossover.techtrial.utils.DateUtils;
import com.crossover.techtrial.utils.JsonSerializer;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kshah
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideControllerTest extends AbstractControllerTest {

    MockMvc mockMvc;
    @Autowired
    RideRepository rideRepository;
    @Autowired
    PersonRepository personRepository;
    @Mock
    private RideController rideController;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private JsonSerializer jsonSerializer;

    private Person driver;
    private Person rider;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(rideController).build();
        //Delete all rides
        rideRepository.deleteAll();

        driver = personRepository.save(Person.newBuilder()
                .setName("SpeedyMan")
                .setEmail("driver@mail.com")
                .build());

        rider = personRepository.save(Person.newBuilder()
                .setName("Mr Harry Up")
                .setEmail("rider@mail.com")
                .build());
    }


    /**
     * @verifies create new ride
     * @see RideController#createNewRide(com.crossover.techtrial.model.Ride)
     */
    @Test
    public void createNewRide_shouldCreateNewRide() throws Exception {
        // given
        final Ride ride = Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(60L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build();
        final HttpEntity<Object> httpEntity = getHttpEntity(jsonSerializer.serialize(ride));

        // when
        final ResponseEntity<Ride> response = template.postForEntity("/api/ride", //
                httpEntity, Ride.class);

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getDriver()).isEqualTo(driver);
        Assertions.assertThat(response.getBody().getRider()).isEqualTo(rider);
        Assertions.assertThat(response.getBody().getDistance()).isEqualTo(ride.getDistance());
        Assertions.assertThat(response.getBody().getStartTime()).isEqualTo(ride.getStartTime());
        Assertions.assertThat(response.getBody().getEndTime()).isEqualTo(ride.getEndTime());
    }

    /**
     * @verifies get a ride by id
     * @see RideController#getRideById(Long)
     */
    @Test
    public void getRideById_shouldGetARideById() throws Exception {
        // given
        final Ride newRide = rideRepository.save(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(60L) //
                .setStartTime(DateUtils.now()) //
                .setEndTime(DateUtils.addHours(DateUtils.now(), 1)) //
                .build());

        // when
        final ResponseEntity<Ride> response = template.getForEntity("/api/ride/{rideId}", //
                Ride.class, newRide.getId());

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isEqualTo(newRide);
        Assertions.assertThat(response.getBody()).isEqualTo(newRide);
    }

    /**
     * @verifies return a not found response
     * @see RideController#getRideById(Long)
     */
    @Test
    public void getRideById_shouldReturnANotFoundResponse() throws Exception {
        // when
        final ResponseEntity<Ride> response = template.getForEntity("/api/ride/{rideId}", //
                Ride.class, "1234");

        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * @verifies return top drivers in time range
     * @see RideController#getTopDriver(Long, java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Test
    public void getTopDriver_shouldReturnTopDriversInTimeRange() throws Exception {
        //given
        final List<Ride> rides = Lists.newArrayList();
        final Instant timeRangeStart = DateUtils.parse("2018-11-25T08:00:00.00Z");
        final Instant timeRangeEnd = DateUtils.parse("2018-11-25T10:00:00.00Z");

        // 2 rides inside time rage
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(60L) //
                .setStartTime(DateUtils.addMinutes(timeRangeStart, 30)) //
                .setEndTime(DateUtils.addMinutes(timeRangeEnd, -30)) //
                .build());
        rides.add(Ride.newBuilder() //
                .setDriver(rider) //
                .setRider(driver) //
                .setDistance(30L) //
                .setStartTime(timeRangeStart) //
                .setEndTime(timeRangeEnd) //
                .build());

        // 4 rides outside time range
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(30L) //
                .setStartTime(DateUtils.addMinutes(timeRangeStart, -30)) //
                .setEndTime(DateUtils.addMinutes(timeRangeStart, 30)) //
                .build());
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(30L) //
                .setStartTime(DateUtils.addMinutes(timeRangeEnd, -30)) //
                .setEndTime(DateUtils.addMinutes(timeRangeStart, 30)) //
                .build());
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(30L) //
                .setStartTime(DateUtils.addHours(timeRangeStart, -1)) //
                .setEndTime(DateUtils.addMinutes(timeRangeStart, -30)) //
                .build());
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(30L) //
                .setStartTime(DateUtils.addMinutes(timeRangeEnd, 30)) //
                .setEndTime(DateUtils.addHours(timeRangeEnd, 1)) //
                .build());

        rideRepository.saveAll(rides);

        // when
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")
                        .withZone(ZoneId.systemDefault());

        final Map<String, Object> params = new HashMap<>();
        params.put("max", 4);
        params.put("start", formatter.format(timeRangeStart));
        params.put("end", formatter.format(timeRangeEnd));

        final ResponseEntity<List<TopDriverDTO>> response = template
                .exchange("/api/top-drivers?max={max}&startTime={start}&endTime={end}", //
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<TopDriverDTO>>() {
                        }, params);
        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(response.getBody()).hasSize(2);
    }

    /**
     * @verifies return top drivers with valid values
     * @see RideController#getTopDriver(Long, java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Test
    public void getTopDriver_shouldReturnTopDriversWithValidValues() throws Exception {
        //given
        final List<Ride> rides = Lists.newArrayList();
        final Instant timeRangeStart = DateUtils.parse("2018-11-25T08:00:00.00Z");
        final Instant timeRangeEnd = DateUtils.parse("2018-11-25T10:00:00.00Z");

        // 4 rides inside time rage
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(60L) //
                .setStartTime(DateUtils.addMinutes(timeRangeStart, 30)) //
                .setEndTime(DateUtils.addMinutes(timeRangeEnd, -30)) //
                .build());
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(30L) //
                .setStartTime(timeRangeStart) //
                .setEndTime(timeRangeEnd) //
                .build());

        rideRepository.saveAll(rides);

        // when
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")
                        .withZone(ZoneId.systemDefault());

        final Map<String, Object> params = new HashMap<>();
        params.put("max", 4);
        params.put("start", formatter.format(timeRangeStart));
        params.put("end", formatter.format(timeRangeEnd));

        final ResponseEntity<List<TopDriverDTO>> response = template
                .exchange("/api/top-drivers?max={max}&startTime={start}&endTime={end}", //
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<TopDriverDTO>>() {
                        }, params);
        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).hasSize(1);
        final TopDriverDTO topDriverDTO = response.getBody().get(0);
        Assertions.assertThat(topDriverDTO.getName()).isEqualTo(driver.getName());
        Assertions.assertThat(topDriverDTO.getEmail()).isEqualTo(driver.getEmail());
        Assertions.assertThat(topDriverDTO.getAverageDistance()).isEqualTo(45L);
        Assertions.assertThat(topDriverDTO.getMaxRideDurationInSeconds()).isEqualTo(7200L);
        Assertions.assertThat(topDriverDTO.getTotalRideDurationInSeconds()).isEqualTo(10800L);
    }

    /**
     * @verifies return top drivers count
     * @see RideController#getTopDriver(Long, java.time.LocalDateTime, java.time.LocalDateTime)
     */
    @Test
    public void getTopDriver_shouldReturnTopDriversCount() throws Exception {
        //given
        final List<Ride> rides = Lists.newArrayList();
        final Instant timeRangeStart = DateUtils.parse("2018-11-25T08:00:00.00Z");
        final Instant timeRangeEnd = DateUtils.parse("2018-11-25T10:00:00.00Z");

        final Person otherDriver = personRepository.save(Person.newBuilder()
                .setName("new driver")
                .setEmail("driver2@mail.com")
                .build());

        // 4 rides inside time rage
        rides.add(Ride.newBuilder() //
                .setDriver(driver) //
                .setRider(rider) //
                .setDistance(60L) //
                .setStartTime(DateUtils.addMinutes(timeRangeStart, 30)) //
                .setEndTime(DateUtils.addMinutes(timeRangeEnd, -30)) //
                .build());
        rides.add(Ride.newBuilder() //
                .setDriver(otherDriver) //
                .setRider(rider) //
                .setDistance(30L) //
                .setStartTime(DateUtils.addMinutes(timeRangeEnd, -30)) //
                .setEndTime(timeRangeEnd) //
                .build());

        rideRepository.saveAll(rides);

        // when
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss")
                        .withZone(ZoneId.systemDefault());

        final Map<String, Object> params = new HashMap<>();
        params.put("max", 1);
        params.put("start", formatter.format(timeRangeStart));
        params.put("end", formatter.format(timeRangeEnd));

        final ResponseEntity<List<TopDriverDTO>> response = template
                .exchange("/api/top-drivers?max={max}&startTime={start}&endTime={end}", //
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<TopDriverDTO>>() {
                        }, params);
        // then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody()).hasSize(1);
    }
}
