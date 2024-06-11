package ru.urfu.squadactivityrating.personalRating.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitingResultsDTO {

    @Enumerated(EnumType.STRING)
    private VisitingResults VisitingResult;
}
