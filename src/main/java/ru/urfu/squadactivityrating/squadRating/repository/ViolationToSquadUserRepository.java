package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;

public interface ViolationToSquadUserRepository extends JpaRepository<ViolationToSquadUser, Long> {
}
