package ru.urfu.squadactivityrating.squadManagement.squadUsers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

public interface SquadUserRepository extends JpaRepository<SquadUser, Long> {
}
