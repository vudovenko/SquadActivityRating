package ru.urfu.squadactivityrating.squadManagement.services;

import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;

import java.util.List;

public interface MembershipApplicationService {

    List<MembershipApplication> getBySquadId(Long id);
}
