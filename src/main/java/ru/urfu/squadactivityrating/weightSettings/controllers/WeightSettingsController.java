package ru.urfu.squadactivityrating.weightSettings.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.squadactivityrating.personalRating.dto.PersonalRatingCoefficientsDto;
import ru.urfu.squadactivityrating.personalRating.entities.PersonalRatingCoefficient;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;
import ru.urfu.squadactivityrating.squadRating.service.PersonalRatingCoefficientService;
import ru.urfu.squadactivityrating.squadRating.service.impl.VisitingResultServiceImpl;
import ru.urfu.squadactivityrating.weightSettings.dto.ViolationTypeDto;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightInSection1Or2;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightInSection5Or6;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightRatingSectionsDto;
import ru.urfu.squadactivityrating.weightSettings.exceptions.SumOfWeightsGreaterThan100Exception;
import ru.urfu.squadactivityrating.weightSettings.services.ViolationTypeService;
import ru.urfu.squadactivityrating.weightSettings.services.WeightSettingsService;

import java.util.List;

@Controller
@RequestMapping("/weight-settings")
@AllArgsConstructor
public class WeightSettingsController {

    private final WeightSettingsService weightSettingsService;
    private final PersonalRatingCoefficientService personalRatingCoefficientService;
    private final VisitingResultServiceImpl visitingResultService;
    private final ViolationTypeService violationTypeService;

    @GetMapping
    public String getWeightSettingsPage(@RequestParam(value = "message", required = false) String message,
                                        Model model) {
        model.addAttribute("message", message);
        model.addAttribute("weightRatingSectionsDto",
                createWeightRatingSectionsDto(weightSettingsService.getWeights()));
        model.addAttribute("personalRatingCoefficientsDto",
                createPersonalRatingCoefficientsDto(weightSettingsService.getPersonalRatingCoefficients()));
        model.addAttribute("weightsInSection1", visitingResultService.getWeightsInSection1());
        model.addAttribute("weightsInSection2", visitingResultService.getWeightsInSection2());
        model.addAttribute("weightsInSection5", visitingResultService.getWeightsInSection5());
        model.addAttribute("weightsInSection6", visitingResultService.getWeightsInSection6());
        model.addAttribute("weightDiscipline", violationTypeService.getViolationTypes());
        return "weightSettings/weight_settings";
    }

    @PostMapping("/update-weight-settings")
    public String updateWeightSettings(WeightRatingSectionsDto weightRatingSectionsDto)
            throws SumOfWeightsGreaterThan100Exception {
        weightSettingsService.updateWeights(weightRatingSectionsDto);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-personal-rating-coefficients")
    public String updatePersonalRatingCoefficients(PersonalRatingCoefficientsDto personalRatingCoefficientsDto) {
        personalRatingCoefficientService.updatePersonalRatingCoefficients(personalRatingCoefficientsDto);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-weight-in-section-1")
    public String updateWeightsInSection1(WeightInSection1Or2 weightInSection1) {
        visitingResultService.updateWeightsInSection1(weightInSection1);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-weight-in-section-2")
    public String updateWeightsInSection2(WeightInSection1Or2 weightInSection2) {
        visitingResultService.updateWeightsInSection2(weightInSection2);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-weight-in-section-5")
    public String updateWeightsInSection5(WeightInSection5Or6 weightInSection5) {
        visitingResultService.updateWeightsInSection5(weightInSection5);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-weight-in-section-6")
    public String updateWeightsInSection6(WeightInSection5Or6 weightInSection6) {
        visitingResultService.updateWeightsInSection6(weightInSection6);
        return "redirect:/weight-settings";
    }

    @PostMapping("/update-violation-types")
    public String updateWeightsInSection7(ViolationTypeDto violationTypeDto) {
        violationTypeService.updateViolationTypes(violationTypeDto);
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
