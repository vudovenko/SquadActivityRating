package ru.urfu.squadactivityrating.eventManagement.services;

import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;

import java.util.List;

public interface EventService {

    List<Event> getEventsByType(EventType eventType);
}
