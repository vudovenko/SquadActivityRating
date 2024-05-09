package ru.urfu.squadactivityrating.eventManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.repositories.EventToSquadUserRepository;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class EventToSquadUserServiceImpl implements EventToSquadUserService {

    private final EventToSquadUserRepository eventToSquadUserRepository;
    @Lazy
    private final EventService eventService;
    private final SquadUserService squadUserService;

    @Override
    public List<EventToSquadUser> getByEventId(Long id) {
        return eventToSquadUserRepository.findByEventId(id);
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
}
