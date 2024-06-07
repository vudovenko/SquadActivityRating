package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadRating.entitites.Violation;

public interface ViolationsRepository extends JpaRepository<Violation, Long> {
}
