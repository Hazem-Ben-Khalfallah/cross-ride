/**
 *
 */
package com.crossover.techtrial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "ride")
public class Ride implements Serializable {

    private static final long serialVersionUID = 9097639215351514001L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_time")
    private Instant startTime;

    @NotNull
    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "distance")
    private Long distance;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Person driver;

    @ManyToOne
    @JoinColumn(name = "rider_id", referencedColumnName = "id")
    private Person rider;

    public Ride() {
    }

    private Ride(Builder builder) {
        setId(builder.id);
        setStartTime(builder.startTime);
        setEndTime(builder.endTime);
        setDistance(builder.distance);
        setDriver(builder.driver);
        setRider(builder.rider);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Person getDriver() {
        return driver;
    }

    public void setDriver(Person driver) {
        this.driver = driver;
    }

    public Person getRider() {
        return rider;
    }

    public void setRider(Person rider) {
        this.rider = rider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return Objects.equals(id, ride.id) &&
                Objects.equals(startTime, ride.startTime) &&
                Objects.equals(endTime, ride.endTime) &&
                Objects.equals(distance, ride.distance) &&
                Objects.equals(driver, ride.driver) &&
                Objects.equals(rider, ride.rider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, distance, driver, rider);
    }


    public static final class Builder {
        private Long id;
        private @NotNull Instant startTime;
        private @NotNull Instant endTime;
        private Long distance;
        private Person driver;
        private Person rider;

        private Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setStartTime(@NotNull Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(@NotNull Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setDistance(Long distance) {
            this.distance = distance;
            return this;
        }

        public Builder setDriver(Person driver) {
            this.driver = driver;
            return this;
        }

        public Builder setRider(Person rider) {
            this.rider = rider;
            return this;
        }

        public Ride build() {
            return new Ride(this);
        }
    }
}
