package ru.urfu.squadactivityrating.eventManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventType_EventType(EventType eventType);
}
