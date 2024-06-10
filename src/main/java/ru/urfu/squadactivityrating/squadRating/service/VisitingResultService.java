package ru.urfu.squadactivityrating.squadRating.service;

import org.springframework.ui.Model;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.squadRating.service.impl.VisitingResultServiceImpl;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Сервис для работы с результатами посещения мероприятий
 */
public interface VisitingResultService {

    LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> getTotalVisitingResultsFromModel(Model model);

    List<Pair<Double, Integer>> getFinalPlacesFromTotalResult(
            LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> totalSquadVisitingResults);

    /**
     * Метод для получения баллов по всем мероприятиям и всем отрядам
     *
     * @param eventTypes тип мероприятия
     * @return список баллов (отряд - мероприятия - результаты участия и баллы за участие)
     */
    VisitingResultServiceImpl.SectionResult<Pair<List<VisitingResult>, Double>> getPointsForEventsWithVisitingResults(EventTypes eventTypes);

    VisitingResultServiceImpl.SectionResult<Duration> getPointsForEventsWithVisitingHours(EventTypes eventTypes);

    <T> List<Event> getEvents(LinkedHashMap<Squad, LinkedHashMap<Event, T>> points);
}
