package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;

/**
 * Сервис для работы с весами разделов рейтинга
 */
public interface WeightRatingSectionsService {

    WeightRatingSections findByEventTypes(EventTypes eventTypes);
}
