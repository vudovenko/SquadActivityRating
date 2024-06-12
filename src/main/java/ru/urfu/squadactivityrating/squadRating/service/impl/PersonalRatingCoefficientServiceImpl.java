package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.PersonalRatingCoefficient;
import ru.urfu.squadactivityrating.squadRating.repository.PersonalRatingCoefficientRepository;
import ru.urfu.squadactivityrating.squadRating.service.PersonalRatingCoefficientService;

import java.util.Optional;

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
}
