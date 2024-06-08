package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadRating.entitites.ViolationType;

/**
 * Репозиторий для работы с типами дисциплинарных нарушений
 */
public interface ViolationsRepository extends JpaRepository<ViolationType, Long> {
}
