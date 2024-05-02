package ru.urfu.squadactivityrating.squadManagement.services;

import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;

import java.util.List;

public interface MembershipApplicationService {

    List<MembershipApplication> getBySquadId(Long id);

    public List<MembershipApplication> getAllMembershipApplications();

    void approveFighter(Long squadId, Long userId);

    void refuseFighter(Long squadId, Long userId);
}
