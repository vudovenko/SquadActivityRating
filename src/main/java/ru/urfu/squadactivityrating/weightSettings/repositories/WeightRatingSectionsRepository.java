package ru.urfu.squadactivityrating.weightSettings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;

//todo склеить с WeightRatingSectionsServiceRepository
@Repository
public interface WeightRatingSectionsRepository extends JpaRepository<WeightRatingSections, Long> {
}
