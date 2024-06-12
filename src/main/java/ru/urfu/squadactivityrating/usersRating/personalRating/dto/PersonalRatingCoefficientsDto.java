package ru.urfu.squadactivityrating.usersRating.personalRating.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonalRatingCoefficientsDto {
    private double coefficient1;
    private double coefficient2;
    private double coefficient3;
    private double coefficient4;
    private double coefficient5;
    private double coefficient6;
    private double coefficient7;
}
