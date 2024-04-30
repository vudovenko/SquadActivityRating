package ru.urfu.squadactivityrating.security.squadUsers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.security.squadUsers.entities.SquadUser;

public interface SquadUserRepository extends JpaRepository<SquadUser, Long> {
}
