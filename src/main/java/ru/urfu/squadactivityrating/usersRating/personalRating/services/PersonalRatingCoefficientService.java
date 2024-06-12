package ru.urfu.squadactivityrating.usersRating.personalRating.services;

import ru.urfu.squadactivityrating.usersRating.personalRating.dto.PersonalRatingCoefficientsDto;

import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.usersRating.personalRating.entities.PersonalRatingCoefficient;

/**
 * Сервис для работы с коэффициентами личного рейтинга бойцов
 */
public interface PersonalRatingCoefficientService {

    PersonalRatingCoefficient getByEventType(EventTypes eventTypes);

    void updatePersonalRatingCoefficients(PersonalRatingCoefficientsDto personalRatingCoefficientsDto);
}
