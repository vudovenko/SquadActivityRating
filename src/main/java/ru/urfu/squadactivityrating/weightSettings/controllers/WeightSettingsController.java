package ru.urfu.squadactivityrating.weightSettings.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightRatingSectionsDto;
import ru.urfu.squadactivityrating.weightSettings.services.WeightSettingsService;

import java.util.List;

@Controller
@RequestMapping("/weight-settings")
@AllArgsConstructor
public class WeightSettingsController {

    private final WeightSettingsService weightSettingsService;

    @GetMapping
    public String getWeightSettingsPage(Model model) {
        List<WeightRatingSections> weights = weightSettingsService.getWeights();
        WeightRatingSectionsDto weightRatingSectionsDto = WeightRatingSectionsDto.builder().build();
        //todo убрать костыль
        for (WeightRatingSections weightRatingSections : weights) {
            if (weightRatingSections.getId() == 1) {
                weightRatingSectionsDto.setWeightRatingSection1(weightRatingSections.getWeightRatingSection());
            } else if (weightRatingSections.getId() == 2) {
                weightRatingSectionsDto.setWeightRatingSection2(weightRatingSections.getWeightRatingSection());
            } else if (weightRatingSections.getId() == 3) {
                weightRatingSectionsDto.setWeightRatingSection3(weightRatingSections.getWeightRatingSection());
            } else if (weightRatingSections.getId() == 4) {
                weightRatingSectionsDto.setWeightRatingSection4(weightRatingSections.getWeightRatingSection());
            } else if (weightRatingSections.getId() == 5) {
                weightRatingSectionsDto.setWeightRatingSection5(weightRatingSections.getWeightRatingSection());
            } else if (weightRatingSections.getId() == 6) {
                weightRatingSectionsDto.setWeightRatingSection6(weightRatingSections.getWeightRatingSection());
            } else {
                weightRatingSectionsDto.setWeightRatingSection7(weightRatingSections.getWeightRatingSection());
            }
        }
        model.addAttribute("weightRatingSectionsDto", weightRatingSectionsDto);
        return "weightSettings/weight_settings";
    }

    @PostMapping("/update-weight-settings")
    public String updateWeightSettings(WeightRatingSectionsDto weightRatingSectionsDto) {
        weightSettingsService.updateWeights(weightRatingSectionsDto);
        return "redirect:/weight-settings";
    }
}
