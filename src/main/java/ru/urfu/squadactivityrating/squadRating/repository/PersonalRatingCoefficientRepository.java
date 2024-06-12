package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.PersonalRatingCoefficient;

import java.util.Optional;

/**
 * Репозиторий для работы с коэффициентами личного рейтинга бойцов
 */
public interface PersonalRatingCoefficientRepository extends JpaRepository<PersonalRatingCoefficient, Long> {

    Optional<PersonalRatingCoefficient> findByEventTypes(EventTypes eventTypes);
}
