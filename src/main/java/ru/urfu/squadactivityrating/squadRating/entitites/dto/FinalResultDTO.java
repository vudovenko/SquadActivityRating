package ru.urfu.squadactivityrating.squadRating.entitites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;

@Data
@AllArgsConstructor
@ToString
public class FinalResultDTO {

    private Double totalPoints;
    private Duration totalHours;
    private Double finalPoints;
    private Integer finalPlace;

    public FinalResultDTO(Double totalPoints, Double finalPoints, Integer finalPlace) {
        this.totalPoints = totalPoints;
        this.finalPoints = finalPoints;
        this.finalPlace = finalPlace;
    }

    public FinalResultDTO(Duration totalHours, Double finalPoints, Integer finalPlace) {
        this.totalHours = totalHours;
        this.finalPoints = finalPoints;
        this.finalPlace = finalPlace;
    }

    public void addToTotalPoints(Double newPoint) {
        totalPoints += newPoint;
    }

    public void addToTotalHours(Duration newHours) {
        totalHours = totalHours.plus(newHours);
    }

    public void addToFinalPoints(Double newPoint) {
        finalPoints += newPoint;
    }
}
