package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;
import ru.urfu.squadactivityrating.squadRating.service.impl.VisitingResultServiceImpl;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Сервис для работы с результатами посещения мероприятий
 */
public interface VisitingHoursService {

    void deleteVisitingHours(VisitingHours visitingHours);
}
