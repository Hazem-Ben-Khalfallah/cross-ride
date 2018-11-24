package com.crossover.techtrial.dto;

/**
 * @author crossover
 */
public class TopDriverDTO {

    private String name;
    private String email;
    private Long totalRideDurationInSeconds;
    private Long maxRideDurationInSeconds;
    private Double averageDistance;

    /**
     * Constructor for TopDriverDTO
     */
    public TopDriverDTO(String name,
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

    public TopDriverDTO() {

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
