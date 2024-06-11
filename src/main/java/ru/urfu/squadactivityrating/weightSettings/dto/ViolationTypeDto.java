package ru.urfu.squadactivityrating.weightSettings.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViolationTypeDto {
    private Double verbalWarning;
    private Double reprimand;
    private Double severeReprimand;
}
