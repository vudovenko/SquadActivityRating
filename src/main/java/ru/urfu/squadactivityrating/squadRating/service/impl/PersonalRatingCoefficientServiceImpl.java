package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.personalRating.dto.PersonalRatingCoefficientsDto;
import ru.urfu.squadactivityrating.personalRating.entities.PersonalRatingCoefficient;
import ru.urfu.squadactivityrating.squadRating.repository.PersonalRatingCoefficientRepository;
import ru.urfu.squadactivityrating.squadRating.service.PersonalRatingCoefficientService;

import java.util.Optional;

import java.lang.reflect.Field;

@Service
@RequiredArgsConstructor
public class PersonalRatingCoefficientServiceImpl implements PersonalRatingCoefficientService {

    private final PersonalRatingCoefficientRepository personalRatingCoefficientRepository;

    @Override
    public PersonalRatingCoefficient getByEventType(EventTypes eventTypes) {
        Optional<PersonalRatingCoefficient> personalRatingCoefficientOptional
                = personalRatingCoefficientRepository.findByEventTypes(eventTypes);

        if (personalRatingCoefficientOptional.isPresent()) {
            return personalRatingCoefficientOptional.get();
        }
        throw new IllegalArgumentException("Коэффициент для типа мероприятия " + eventTypes + " не найден");
    }

    @Override
    public void updatePersonalRatingCoefficients(PersonalRatingCoefficientsDto personalRatingCoefficientsDto) {
        Field[] fields = PersonalRatingCoefficientsDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            long id = Character.getNumericValue(fieldName.charAt(fieldName.length() - 1));
            Optional<PersonalRatingCoefficient> personalRatingCoefficient = personalRatingCoefficientRepository.findById(id);
            if (personalRatingCoefficient.isPresent()) {
                try {
                    personalRatingCoefficient.get().setPersonalRatingCoefficient((double)field.get(personalRatingCoefficientsDto));
                    personalRatingCoefficientRepository.save(personalRatingCoefficient.get());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
