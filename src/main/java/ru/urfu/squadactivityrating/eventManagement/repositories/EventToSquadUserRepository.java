package ru.urfu.squadactivityrating.eventManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с посещениями событий участниками
 */
public interface EventToSquadUserRepository extends JpaRepository<EventToSquadUser, Long> {

    /**
     * Метод поиска посещения по id события
     *
     * @param id идентификатор события
     * @return список посещений
     */
    List<EventToSquadUser> findByEventId(Long id);

    /**
     * Метод поиска посещения по id события и id участника
     *
     * @param eventId     идентификатор события
     * @param squadUserId идентификатор участника
     * @return посещение
     */
    Optional<EventToSquadUser> findByEventIdAndSquadUserId(Long eventId, Long squadUserId);

    boolean existsByEventIdAndSquadUserId(Long eventId, Long squadUserId);
}
