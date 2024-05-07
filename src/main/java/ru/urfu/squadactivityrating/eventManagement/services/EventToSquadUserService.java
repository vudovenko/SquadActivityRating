package ru.urfu.squadactivityrating.eventManagement.services;

import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;

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
}
