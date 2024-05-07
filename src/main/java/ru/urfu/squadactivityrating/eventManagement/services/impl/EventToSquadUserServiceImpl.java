package ru.urfu.squadactivityrating.eventManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.repositories.EventToSquadUserRepository;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventToSquadUserServiceImpl implements EventToSquadUserService {

    private final EventToSquadUserRepository eventToSquadUserRepository;

    @Override
    public List<EventToSquadUser> getByEventId(Long id) {
        return eventToSquadUserRepository.findByEventId(id);
    }

    @Override
    public void deleteAllEventsToSquadUsers(List<EventToSquadUser> eventsToSquadUsers) {
        eventToSquadUserRepository.deleteAll(eventsToSquadUsers);
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
}
