package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;

import java.util.List;

/**
 * Репозиторий для работы с результатами посещения мероприятий
 */
public interface VisitingResultRepository extends JpaRepository<VisitingResult, Long> {

    List<VisitingResult> findByVisits_Event_EventType_EventTypeValue(EventTypes eventTypes);
}
