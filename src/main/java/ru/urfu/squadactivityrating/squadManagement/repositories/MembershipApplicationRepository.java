package ru.urfu.squadactivityrating.squadManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с заявками {@link MembershipApplication}
 */
public interface MembershipApplicationRepository extends JpaRepository<MembershipApplication, Long> {

    /**
     * Метод поиска заявок по id отряда
     *
     * @param id идентификатор отряда
     * @return список заявок
     */
    List<MembershipApplication> findBySquadId(Long id);

    /**
     * Метод поиска заявки по id отряда и id пользователя
     *
     * @param squadId идентификатор отряда
     * @param userId  идентификатор пользователя
     * @return заявка в объекте типа {@link Optional}
     */
    Optional<MembershipApplication> findBySquadIdAndSquadUserId(Long squadId, Long userId);
}
