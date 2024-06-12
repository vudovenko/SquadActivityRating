package ru.urfu.squadactivityrating.weightSettings.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.usersRating.personalRating.entities.PersonalRatingCoefficient;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;
import ru.urfu.squadactivityrating.squadRating.repository.PersonalRatingCoefficientRepository;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightRatingSectionsDto;
import ru.urfu.squadactivityrating.weightSettings.exceptions.SumOfWeightsGreaterThan100Exception;
import ru.urfu.squadactivityrating.weightSettings.repositories.WeightRatingSectionsRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

//todo склеить с WeightRatingSectionsServiceRepository
@Service
@AllArgsConstructor
public class WeightSettingsService {

    private final WeightRatingSectionsRepository weightRatingSectionsRepository;
    private final PersonalRatingCoefficientRepository personalRatingCoefficientRepository;

    public List<WeightRatingSections> getWeights() {
        return weightRatingSectionsRepository.findAll();
    }

    public void updateWeights(WeightRatingSectionsDto weightRatingSectionsDto)
            throws SumOfWeightsGreaterThan100Exception {
        if (sumOfWeights(weightRatingSectionsDto) > 100) {
            throw new SumOfWeightsGreaterThan100Exception("Сумма весов должна быть равна 100: перебор процентов = "
                    + -(100 - sumOfWeights(weightRatingSectionsDto)) + "%");
        } else if (sumOfWeights(weightRatingSectionsDto) < 100) {
            throw new SumOfWeightsGreaterThan100Exception("Сумма весов должна быть равна 100: недобор процентов = "
                    + (100 - sumOfWeights(weightRatingSectionsDto)) + "%");
        }
        Field[] fields = WeightRatingSectionsDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            long id = Character.getNumericValue(fieldName.charAt(fieldName.length() - 1));
            Optional<WeightRatingSections> weightRatingSections = weightRatingSectionsRepository.findById(id);
            if (weightRatingSections.isPresent()) {
                try {
                    weightRatingSections.get().setWeightRatingSection((int)field.get(weightRatingSectionsDto));
                    weightRatingSectionsRepository.save(weightRatingSections.get());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<PersonalRatingCoefficient> getPersonalRatingCoefficients() {
        return personalRatingCoefficientRepository.findAll();
    }

    private static int sumOfWeights(WeightRatingSectionsDto weightRatingSectionsDto) {
        return weightRatingSectionsDto.getWeightRatingSection1()
                + weightRatingSectionsDto.getWeightRatingSection2()
                + weightRatingSectionsDto.getWeightRatingSection3()
                + weightRatingSectionsDto.getWeightRatingSection4()
                + weightRatingSectionsDto.getWeightRatingSection5()
                + weightRatingSectionsDto.getWeightRatingSection6();
    }
}
