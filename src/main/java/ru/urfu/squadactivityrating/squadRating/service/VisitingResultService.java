package ru.urfu.squadactivityrating.squadRating.service;

import org.springframework.ui.Model;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;

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
     * Метод для установки результатов по типу посещения в модель
     *
     * @param eventTypes тип мероприятия
     * @param model      модель
     */
    void setVisitingResultsInModel(EventTypes eventTypes, Model model);
}
