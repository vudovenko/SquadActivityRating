package ru.urfu.squadactivityrating.eventManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.EventType;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.repositories.EventRepository;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventToSquadUserService eventToSquadUserService;
    private final SquadUserService squadUserService;

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
            throw new IllegalArgumentException("Event with id " + eventId + " not found");
        }
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event saveEvent(Event event,
                           Integer hoursDuration, Integer minutesDuration,
                           String eventType, Long[] selectedFightersIds) {
        event.setDuration(Duration.ofHours(hoursDuration).plusMinutes(minutesDuration));
        EventType eventTypeObj = new EventType();
        EventTypes eventTypes = EventTypes.valueOf(eventType);
        eventTypeObj.setEventTypeValue(eventTypes);
        event.setEventType(eventTypeObj);
        List<SquadUser> selectedFighters = squadUserService.getUsersByIds(selectedFightersIds);

        Event eventEntity = saveEvent(event);
        selectedFighters.forEach(f -> {
            EventToSquadUser eventToSquadUser = new EventToSquadUser();
            eventToSquadUser.setEvent(eventEntity);
            eventToSquadUser.setSquadUser(f);
            eventToSquadUserService.save(eventToSquadUser);
        });

        return eventEntity;
    }
}
