package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.PersonalRatingCoefficient;

/**
 * Сервис для работы с коэффициентами личного рейтинга бойцов
 */
public interface PersonalRatingCoefficientService {

    PersonalRatingCoefficient getByEventType(EventTypes eventTypes);
}
