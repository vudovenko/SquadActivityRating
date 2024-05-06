package ru.urfu.squadactivityrating.eventManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;
import ru.urfu.squadactivityrating.eventManagement.repositories.EventRepository;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getEventsByType(EventType eventType) {
        return eventRepository.findByEventType_EventType(eventType);
    }
}
