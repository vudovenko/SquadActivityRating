package ru.urfu.squadactivityrating.squadRating.service;

import org.springframework.ui.Model;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;

/**
 * Сервис для работы с результатами посещения мероприятий
 */
public interface VisitingResultService {

    /**
     * Метод для установки результатов посещения в модель
     *
     * @param eventTypes тип мероприятия
     * @param model      модель
     */
    void setVisitingResultsInModel(EventTypes eventTypes, Model model);
}
