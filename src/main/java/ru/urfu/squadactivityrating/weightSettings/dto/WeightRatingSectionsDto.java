package ru.urfu.squadactivityrating.weightSettings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WeightRatingSectionsDto {
    private int weightRatingSection1;
    private int weightRatingSection2;
    private int weightRatingSection3;
    private int weightRatingSection4;
    private int weightRatingSection5;
    private int weightRatingSection6;
}
