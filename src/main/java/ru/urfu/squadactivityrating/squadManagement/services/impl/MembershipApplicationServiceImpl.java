package ru.urfu.squadactivityrating.squadManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;
import ru.urfu.squadactivityrating.squadManagement.repositories.MembershipApplicationRepository;
import ru.urfu.squadactivityrating.squadManagement.services.MembershipApplicationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipApplicationServiceImpl implements MembershipApplicationService {

    private final MembershipApplicationRepository membershipApplicationRepository;

    @Override
    public List<MembershipApplication> getBySquadId(Long id) {
        return membershipApplicationRepository.findBySquadId(id);
    }
}
