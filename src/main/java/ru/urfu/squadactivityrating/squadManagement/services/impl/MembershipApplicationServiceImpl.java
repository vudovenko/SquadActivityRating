package ru.urfu.squadactivityrating.squadManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.repositories.MembershipApplicationRepository;
import ru.urfu.squadactivityrating.squadManagement.services.MembershipApplicationService;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipApplicationServiceImpl implements MembershipApplicationService {

    private final MembershipApplicationRepository membershipApplicationRepository;
    private final SquadService squadService;
    private final SquadUserService squadUserService;

    @Override
    public List<MembershipApplication> getBySquadId(Long id) {
        return membershipApplicationRepository.findBySquadId(id);
    }

    @Override
    public List<MembershipApplication> getAllMembershipApplications() {
        return membershipApplicationRepository.findAll();
    }

    @Override
    public void submitApplication(Long squadId, Long userId) {
        MembershipApplication membershipApplication = MembershipApplication
                .builder()
                .squad(squadService.getSquadById(squadId))
                .squadUser(squadUserService.getUserById(userId))
                .build();
        if (membershipApplicationRepository
                .findBySquadIdAndSquadUserId(squadId, userId).isEmpty()) {
            membershipApplicationRepository.save(membershipApplication);
        }
    }

    @Override
    public void approveFighter(Long squadId, Long userId) {
        Optional<MembershipApplication> membershipApplicationOptional =
                membershipApplicationRepository.findBySquadIdAndSquadUserId(squadId, userId);
        if (membershipApplicationOptional.isPresent()) {
            Squad squad = squadService.getSquadById(squadId);
            SquadUser squadUser = squadUserService.getUserById(userId);
            squadUser.setSquad(squad);

            squadUserService.saveUser(squadUser);
            deleteMembershipApplicationOptional(membershipApplicationOptional);
        } else {
            throw new IllegalArgumentException("Membership application not found");
        }
    }

    @Override
    public void refuseFighter(Long squadId, Long userId) {
        Optional<MembershipApplication> membershipApplicationOptional =
                membershipApplicationRepository.findBySquadIdAndSquadUserId(squadId, userId);

        deleteMembershipApplicationOptional(membershipApplicationOptional);
    }

    private void deleteMembershipApplicationOptional(Optional<MembershipApplication> membershipApplicationOptional) {
        if (membershipApplicationOptional.isPresent()) {
            membershipApplicationRepository.delete(membershipApplicationOptional.get());
        } else {
            throw new IllegalArgumentException("Membership application not found");
        }
    }
}
