package ru.urfu.squadactivityrating.weightSettings.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.entitites.ViolationType;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.ViolationTypes;
import ru.urfu.squadactivityrating.weightSettings.dto.ViolationTypeDto;
import ru.urfu.squadactivityrating.weightSettings.repositories.ViolationTypeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ViolationTypeService {

    private final ViolationTypeRepository violationTypeRepository;

    public ViolationTypeDto getViolationTypes() {
        ViolationTypeDto violationTypeDto = ViolationTypeDto.builder().build();
        List<ViolationType> violationTypes = violationTypeRepository.findAll();
        for (ViolationType violationType : violationTypes) {
            if (violationType.getViolation().equals(ViolationTypes.REPRIMAND)) {
                violationTypeDto.setReprimand(violationType.getWeight());
            } else if (violationType.getViolation().equals(ViolationTypes.VERBAL_WARNING)) {
                violationTypeDto.setVerbalWarning(violationType.getWeight());
            } else {
                violationTypeDto.setSevereReprimand(violationType.getWeight());
            }
        }
        return violationTypeDto;
    }

    public void updateViolationTypes(ViolationTypeDto violationTypeDto) {
        List<ViolationType> violationTypes = violationTypeRepository.findAll();
        for (ViolationType violationType : violationTypes) {
            if (violationType.getViolation().equals(ViolationTypes.REPRIMAND)) {
                violationType.setWeight(violationTypeDto.getReprimand());
            } else if (violationType.getViolation().equals(ViolationTypes.VERBAL_WARNING)) {
                violationType.setWeight(violationTypeDto.getVerbalWarning());
            } else {
                violationType.setWeight(violationTypeDto.getSevereReprimand());
            }
            violationTypeRepository.save(violationType);
        }
    }
}
