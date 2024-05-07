package ru.urfu.squadactivityrating.eventManagement.services;

import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;

import java.util.List;

/**
 * Сервис для работы с событиями
 */
public interface EventService {

    /**
     * Метод получения всех событий по типу события
     *
     * @param eventType тип события
     * @return список событий
     */
    List<Event> getEventsByType(EventType eventType);

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
}
