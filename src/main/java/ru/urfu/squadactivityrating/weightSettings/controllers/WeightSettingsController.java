package ru.urfu.squadactivityrating.weightSettings.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.personalRating.dto.PersonalRatingCoefficientsDto;
import ru.urfu.squadactivityrating.personalRating.entities.PersonalRatingCoefficient;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;
import ru.urfu.squadactivityrating.squadRating.service.PersonalRatingCoefficientService;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightRatingSectionsDto;
import ru.urfu.squadactivityrating.weightSettings.services.WeightSettingsService;

import java.util.List;

@Controller
@RequestMapping("/weight-settings")
@AllArgsConstructor
public class WeightSettingsController {

    private final WeightSettingsService weightSettingsService;
    private final PersonalRatingCoefficientService personalRatingCoefficientService;

    @GetMapping
    public String getWeightSettingsPage(Model model) {
        model.addAttribute("weightRatingSectionsDto",
                createWeightRatingSectionsDto(weightSettingsService.getWeights()));
        model.addAttribute("personalRatingCoefficientsDto",
                createPersonalRatingCoefficientsDto(weightSettingsService.getPersonalRatingCoefficients()));
        return "weightSettings/weight_settings";
    }

    @PostMapping("/update-weight-settings")
    public String updateWeightSettings(WeightRatingSectionsDto weightRatingSectionsDto) {
        weightSettingsService.updateWeights(weightRatingSectionsDto);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-personal-rating-coefficients")
    public String updatePersonalRatingCoefficients(PersonalRatingCoefficientsDto personalRatingCoefficientsDto) {
        personalRatingCoefficientService.updatePersonalRatingCoefficients(personalRatingCoefficientsDto);
        return "redirect:/weight-settings";
    }

    private static WeightRatingSectionsDto createWeightRatingSectionsDto(List<WeightRatingSections> weights) {
        WeightRatingSectionsDto weightRatingSectionsDto = WeightRatingSectionsDto.builder().build();
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
        return weightRatingSectionsDto;
    }

    private static PersonalRatingCoefficientsDto createPersonalRatingCoefficientsDto(
            List<PersonalRatingCoefficient> coefficients) {
        PersonalRatingCoefficientsDto personalRatingCoefficientsDto = PersonalRatingCoefficientsDto
                .builder()
                .build();
        for (PersonalRatingCoefficient coefficient : coefficients) {
            if (coefficient.getId() == 1) {
                personalRatingCoefficientsDto.setCoefficient1(coefficient.getPersonalRatingCoefficient());
            } else if (coefficient.getId() == 2) {
                personalRatingCoefficientsDto.setCoefficient2(coefficient.getPersonalRatingCoefficient());
            } else if (coefficient.getId() == 3) {
                personalRatingCoefficientsDto.setCoefficient3(coefficient.getPersonalRatingCoefficient());
            } else if (coefficient.getId() == 4) {
                personalRatingCoefficientsDto.setCoefficient4(coefficient.getPersonalRatingCoefficient());
            } else if (coefficient.getId() == 5) {
                personalRatingCoefficientsDto.setCoefficient5(coefficient.getPersonalRatingCoefficient());
            } else if (coefficient.getId() == 6) {
                personalRatingCoefficientsDto.setCoefficient6(coefficient.getPersonalRatingCoefficient());
            } else {
                personalRatingCoefficientsDto.setCoefficient7(coefficient.getPersonalRatingCoefficient());
            }
        }
        return personalRatingCoefficientsDto;
    }
}
