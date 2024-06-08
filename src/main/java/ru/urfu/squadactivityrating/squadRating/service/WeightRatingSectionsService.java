package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;

/**
 * Сервис для работы с весами разделов рейтинга
 */
public interface WeightRatingSectionsService {

    /**
     * Метод для получения веса типа мероприятия по указанному типу мероприятия
     *
     * @param eventTypes тип мероприятия для фильтрации
     * @return сущность веса
     */
    WeightRatingSections findByEventTypes(EventTypes eventTypes);
}
