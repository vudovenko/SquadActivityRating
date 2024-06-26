package ru.urfu.squadactivityrating.squadRating.entitites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;

import static ru.urfu.squadactivityrating.squadRating.service.impl.VisitingResultServiceImpl.round;

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

    public FinalResultDTO() {
        totalPoints = 0.0;
        totalHours = Duration.ZERO;
        finalPoints = 0.0;
        finalPlace = 0;
    }

    public void addToTotalPoints(Double newPoint) {
        totalPoints += newPoint;
        totalPoints = round(totalPoints, 1);
    }

    public void addToTotalHours(Duration newHours) {
        totalHours = totalHours.plus(newHours);
    }

    public void addToFinalPoints(Double newPoint) {
        finalPoints += newPoint;
        finalPoints = round(finalPoints, 1);
    }
}
