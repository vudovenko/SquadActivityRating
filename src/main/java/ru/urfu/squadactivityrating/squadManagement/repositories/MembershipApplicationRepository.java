package ru.urfu.squadactivityrating.squadManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;

import java.util.List;
import java.util.Optional;

public interface MembershipApplicationRepository extends JpaRepository<MembershipApplication, Long> {

    List<MembershipApplication> findBySquadId(Long id);

    Optional<MembershipApplication> findBySquadIdAndSquadUserId(Long squadId, Long userId);
}
