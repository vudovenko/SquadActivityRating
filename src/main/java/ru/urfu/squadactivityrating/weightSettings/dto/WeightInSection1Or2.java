package ru.urfu.squadactivityrating.weightSettings.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeightInSection1Or2 {
    private Double place1;
    private Double place2;
    private Double place3;
    private Double participation;
}
