package ru.urfu.squadactivityrating.eventManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;

import java.util.List;

/**
 * Репозиторий событий
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Метод получения событий по типу события
     *
     * @param eventType тип события
     * @return список событий
     */
    List<Event> findByEventType_EventType(EventType eventType);
}
