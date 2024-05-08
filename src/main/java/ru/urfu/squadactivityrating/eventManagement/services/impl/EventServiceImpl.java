package ru.urfu.squadactivityrating.eventManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.repositories.EventRepository;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventToSquadUserService eventToSquadUserService;

    @Override
    public List<Event> getEventsByType(EventTypes eventTypes) {
        return eventRepository.findByEventType_EventTypeValue(eventTypes);
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with id " + id + " not found"));
    }

    @Override
    public void deleteEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event eventEntity = eventOptional.get();
            List<EventToSquadUser> eventToSquadUsers
                    = eventToSquadUserService.getByEventId(eventId);
            eventToSquadUserService.deleteAllEventsToSquadUsers(eventToSquadUsers);
            eventRepository.delete(eventEntity);
        } else {
            throw new IllegalArgumentException("Squad not found");
        }
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
}
