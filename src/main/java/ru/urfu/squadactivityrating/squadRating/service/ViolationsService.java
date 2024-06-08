package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.squadRating.entitites.ViolationType;

import java.util.List;

/**
 * Сервис для работы с дисциплинарными нарушениями
 */
public interface ViolationsService {

    /**
     * Метод для получения списка дисциплинарных нарушений
     *
     * @return список дисциплинарных нарушений
     */
    List<ViolationType> getAll();
}
