package ru.urfu.squadactivityrating.eventManagement.services;

import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;

import java.util.List;

/**
 * Сервис для работы с событиями
 */
public interface EventService {

    /**
     * Метод получения всех событий по типу события
     *
     * @param eventTypes тип события
     * @return список событий
     */
    List<Event> getEventsByType(EventTypes eventTypes);

    /**
     * Метод получения события по идентификатору
     *
     * @param id идентификатор
     * @return событие
     */
    Event getEventById(Long id);

    /**
     * Метод удаления события
     *
     * @param squadId идентификатор
     */
    void deleteEvent(Long squadId);

    Event saveEvent(Event event);
}
