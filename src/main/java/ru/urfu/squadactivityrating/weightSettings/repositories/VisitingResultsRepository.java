package ru.urfu.squadactivityrating.weightSettings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;

@Repository
public interface VisitingResultsRepository extends JpaRepository<VisitingResult, Long> {
}
