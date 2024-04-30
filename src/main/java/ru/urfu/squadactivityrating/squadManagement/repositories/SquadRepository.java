package ru.urfu.squadactivityrating.squadManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;

public interface SquadRepository extends JpaRepository<Squad, Long> {
}
