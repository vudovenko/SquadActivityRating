package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.personalRating.dto.PersonalRatingCoefficientsDto;

/**
 * Сервис для работы с коэффициентами личного рейтинга бойцов
 */
public interface PersonalRatingCoefficientService {
    void updatePersonalRatingCoefficients(PersonalRatingCoefficientsDto personalRatingCoefficientsDto);
}
