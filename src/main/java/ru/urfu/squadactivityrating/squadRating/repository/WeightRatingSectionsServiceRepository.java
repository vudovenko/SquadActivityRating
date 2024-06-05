package ru.urfu.squadactivityrating.squadRating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;

/**
 * Репозиторий для работы с весами разделов рейтинга
 */
public interface WeightRatingSectionsServiceRepository extends JpaRepository<WeightRatingSections, Long> {
}
