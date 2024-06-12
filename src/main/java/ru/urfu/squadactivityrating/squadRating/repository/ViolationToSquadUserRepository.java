package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;

import java.util.List;

/**
 * Репозиторий для работы с сущностями связей между нарушениями и бойцами
 */
public interface ViolationToSquadUserRepository extends JpaRepository<ViolationToSquadUser, Long> {

    List<ViolationToSquadUser> findAllByIsSolved(Boolean isSolved);

    List<ViolationToSquadUser> findAllByViolator_SquadAndIsSolved(Squad squad, Boolean isSolved);

    List<ViolationToSquadUser> findAllByViolatorAndIsSolved(SquadUser squadUser, Boolean isSolved);
}
