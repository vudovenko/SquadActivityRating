package ru.urfu.squadactivityrating.squadRating.service;

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

    VisitingResultServiceImpl.SectionResult<EventTypes, Double> getTotalPointsForAllEvents();

    /**
     * Метод для получения баллов по всем мероприятиям и всем отрядам
     *
     * @param eventTypes тип мероприятия
     * @return список баллов (отряд - мероприятия - результаты участия и баллы за участие)
     */
    VisitingResultServiceImpl.SectionResult<Event, Pair<List<VisitingResult>, Double>> getPointsForEventsWithVisitingResults(EventTypes eventTypes);

    VisitingResultServiceImpl.SectionResult<Event, Duration> getPointsForEventsWithVisitingHours(EventTypes eventTypes);

    <U, T> List<U> getEventsAndTypes(LinkedHashMap<Squad, LinkedHashMap<U, T>> points);
}
