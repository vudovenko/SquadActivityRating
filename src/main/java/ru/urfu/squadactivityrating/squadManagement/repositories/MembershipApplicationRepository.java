package ru.urfu.squadactivityrating.squadManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;

import java.util.List;

public interface MembershipApplicationRepository extends JpaRepository<MembershipApplication, Long> {

    List<MembershipApplication> findBySquadId(Long id);
}
