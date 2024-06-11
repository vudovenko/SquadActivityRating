package ru.urfu.squadactivityrating.personalRating.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitingResultsDTO {

    @Enumerated(EnumType.STRING)
    private VisitingResults VisitingResult;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    public VisitingResultsDTO(VisitingResults visitingResults) {
        this.VisitingResult = visitingResults;
    }

    public VisitingResultsDTO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
