package ru.urfu.squadactivityrating.eventManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;

import java.util.List;

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
}
