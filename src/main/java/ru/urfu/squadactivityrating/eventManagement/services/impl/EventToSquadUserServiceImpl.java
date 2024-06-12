package ru.urfu.squadactivityrating.eventManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.repositories.EventToSquadUserRepository;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class EventToSquadUserServiceImpl implements EventToSquadUserService {

    private final EventToSquadUserRepository eventToSquadUserRepository;
    @Lazy
    private final EventService eventService;
    private final SquadUserService squadUserService;

    @Override
    public EventToSquadUser getById(Long id) {
        Optional<EventToSquadUser> eventToSquadUserOptional = eventToSquadUserRepository.findById(id);

        if (eventToSquadUserOptional.isPresent()) {
            return eventToSquadUserOptional.get();
        } else {
            throw new IllegalArgumentException("EventToSquadUser с id " + id + " не существует");
        }
    }

    @Override
    public List<EventToSquadUser> getByEventId(Long id) {
        return eventToSquadUserRepository.findByEventId(id);
    }

    @Override
    public List<EventToSquadUser> getBySquadUserId(Long id) {
        return eventToSquadUserRepository.findBySquadUserId(id);
    }

    @Override
    public List<EventToSquadUser> getAllEventsToSquadUsersWhereVisitingResultNotNull() {
        return eventToSquadUserRepository.findAllByVisitingResultNotNull();
    }

    @Override
    public List<EventToSquadUser> getEventsToSquadUsersByEventType(EventTypes eventTypes) {
        return eventToSquadUserRepository
                .findByEvent_EventType_EventTypeValue(eventTypes);
    }

    @Override
    public void deleteAllEventsToSquadUsers(List<EventToSquadUser> eventsToSquadUsers) {
        eventToSquadUserRepository.deleteAll(eventsToSquadUsers);
    }

    @Override
    public void deleteEventToSquadUser(EventToSquadUser eventToSquadUser) {
        eventToSquadUserRepository.delete(eventToSquadUser);
    }

    @Override
    public EventToSquadUser getEventToSquadUserByEventIdAndSquadUserId(Long eventId,
                                                                       Long squadUserId) {
        return eventToSquadUserRepository
                .findByEventIdAndSquadUserId(eventId, squadUserId)
                .orElseThrow(
                        () -> new IllegalArgumentException("EventToSquadUser with eventId " + eventId
                                + " and squadUserId " + squadUserId + " not found"));
    }

    @Override
    public EventToSquadUser save(EventToSquadUser eventToSquadUser) {
        return eventToSquadUserRepository.save(eventToSquadUser);
    }

    @Override
    public void subscribeForEvent(SecurityUser securityUser, Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (!event.getParticipants().contains(securityUser.getSquadUser())) {
            EventToSquadUser eventToSquadUser = new EventToSquadUser();
            eventToSquadUser.setEvent(event);
            SquadUser squadUser = squadUserService
                    .getUserById(securityUser.getSquadUser().getId());
            eventToSquadUser.setSquadUser(squadUser);
            save(eventToSquadUser);
        }
    }

    @Override
    public void unsubscribeFromEvent(SecurityUser securityUser, Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event.getParticipants().contains(securityUser.getSquadUser())) {
            SquadUser squadUser = securityUser.getSquadUser();
            List<EventToSquadUser> eventToSquadUsers = getByEventId(eventId);
            eventToSquadUsers.forEach(e -> {
                if (e.getSquadUser().equals(squadUser)) {
                    EventToSquadUser eventToSquadUser =
                            getEventToSquadUserByEventIdAndSquadUserId(eventId, squadUser.getId());
                    deleteEventToSquadUser(eventToSquadUser);
                }
            });
        }
    }

    @Override
    public EventToSquadUser getNewVisiting(Event event) {
        EventTypes eventTypes = event.getEventType().getEventTypeValue();
        EventToSquadUser eventToSquadUser = new EventToSquadUser();
        eventToSquadUser.setEvent(event);
        VisitingResult visitingResult = new VisitingResult();
        VisitingHours visitingHours = new VisitingHours();
        if (eventTypes.equals(EventTypes.SOCIAL_WORK)
                || eventTypes.equals(EventTypes.PRODUCTION_WORK)) {
            visitingHours.setStartTime(event.getDate());
            visitingHours.setEndTime(event.getDate().plus(event.getDuration()));
            eventToSquadUser.setVisitingHours(visitingHours);
        } else if (eventTypes == EventTypes.SPORT
                || eventTypes == EventTypes.CREATIVE_WORK) {
            visitingResult.setVisitingResult(VisitingResults.PARTICIPATION);
            eventToSquadUser.setVisitingResult(visitingResult);
        } else if (eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU) {
            visitingResult.setVisitingResult(VisitingResults.PRESENCE);
            eventToSquadUser.setVisitingResult(visitingResult);
        }

        return eventToSquadUser;
    }
}
