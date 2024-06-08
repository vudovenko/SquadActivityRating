package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;

import java.util.List;

/**
 * Репозиторий для работы с сущностями связей между нарушениями и бойцами
 */
public interface ViolationToSquadUserRepository extends JpaRepository<ViolationToSquadUser, Long> {

    List<ViolationToSquadUser> findAllByIsSolved(Boolean isSolved);
}
