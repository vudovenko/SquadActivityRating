package ru.urfu.squadactivityrating.eventManagement.services;

import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;

import java.util.List;

/**
 * Сервис для работы с посещениями событий участниками
 */
public interface EventToSquadUserService {

    /**
     * Метод поиска посещения по id события
     *
     * @param id идентификатор события
     * @return список посещений
     */
    List<EventToSquadUser> getByEventId(Long id);

    /**
     * Метод удаления всех переданных посещений
     *
     * @param eventsToSquadUsers посещения для удаления
     */
    void deleteAllEventsToSquadUsers(List<EventToSquadUser> eventsToSquadUsers);

    void deleteEventToSquadUser(EventToSquadUser eventToSquadUser);

    /**
     * Метод поиска посещения по id события и id участника
     *
     * @param eventId     идентификатор события
     * @param squadUserId идентификатор участника
     * @return посещение
     */
    EventToSquadUser getEventToSquadUserByEventIdAndSquadUserId(Long eventId, Long squadUserId);

    EventToSquadUser save(EventToSquadUser eventToSquadUser);

    void subscribeForEvent(SecurityUser securityUser, Long eventId);

    void unsubscribeFromEvent(SecurityUser securityUser, Long eventId);
}
