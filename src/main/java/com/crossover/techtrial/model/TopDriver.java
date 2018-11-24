package com.crossover.techtrial.model;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

/**
 * @author hazem
 */
@Entity
@SqlResultSetMapping(
        name = "TopRidersMapping",
        classes = {
                @ConstructorResult(
                        targetClass = TopDriver.class,
                        columns = {
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "email", type = String.class),
                                @ColumnResult(name = "totalDuration", type = Long.class),
                                @ColumnResult(name = "maxDuration", type = Long.class),
                                @ColumnResult(name = "averageDistance", type = Double.class)
                        }
                )
        }
)
@NamedNativeQuery(name = "TopDriver.getTopRiders",
        query = "select p.name, p.email, " +
                "Sum(UNIX_TIMESTAMP(e.end_time) - UNIX_TIMESTAMP(e.start_time)) as totalDuration, " +
                "    MAX(UNIX_TIMESTAMP(e.end_time) - UNIX_TIMESTAMP(e.start_time)) as maxDuration, " +
                "   :startTime as startTime, :endTime as endTime, " +
                "    AVG(e.distance) as averageDistance " +
                "from person p " +
                "inner join ride e on p.id = e.driver_id " +
                "where e.start_time BETWEEN :startTime AND :endTime " +
                "   AND e.end_time BETWEEN :startTime AND :endTime " +
                "group by p.id " +
                "order by totalDuration desc " +
                "limit :count ",
        resultSetMapping = "TopRidersMapping")
public class TopDriver {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    private String name;
    private String email;
    private Long totalRideDurationInSeconds;
    private Long maxRideDurationInSeconds;
    private Double averageDistance;

    /**
     * Constructor for TopDriverDTO
     */
    public TopDriver(String name,
                     String email,
                     Long totalRideDurationInSeconds,
                     Long maxRideDurationInSeconds,
                     Double averageDistance) {
        this.setName(name);
        this.setEmail(email);
        this.setAverageDistance(averageDistance);
        this.setMaxRideDurationInSeconds(maxRideDurationInSeconds);
        this.setTotalRideDurationInSeconds(totalRideDurationInSeconds);

    }

    public TopDriver() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTotalRideDurationInSeconds() {
        return totalRideDurationInSeconds;
    }

    public void setTotalRideDurationInSeconds(Long totalRideDurationInSeconds) {
        this.totalRideDurationInSeconds = totalRideDurationInSeconds;
    }

    public Long getMaxRideDurationInSeconds() {
        return maxRideDurationInSeconds;
    }

    public void setMaxRideDurationInSeconds(Long maxRideDurationInSeconds) {
        this.maxRideDurationInSeconds = maxRideDurationInSeconds;
    }

    public Double getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(Double averageDistance) {
        this.averageDistance = averageDistance;
    }

}
