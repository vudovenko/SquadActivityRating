package ru.urfu.squadactivityrating.squadRating.entitites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class FinalResultDTO {

    private Double totalPoints;
    private Double finalPoints;
    private Integer finalPlace;

    public void addToTotalPoints(Double newPoint) {
        totalPoints += newPoint;
    }

    public void addToFinalPoints(Double newPoint) {
        finalPoints += newPoint;
    }
}
