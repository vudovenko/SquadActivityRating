package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;
import ru.urfu.squadactivityrating.squadRating.repository.ViolationToSquadUserRepository;
import ru.urfu.squadactivityrating.squadRating.service.ViolationToSquadUserService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ViolationToSquadUserServiceImpl implements ViolationToSquadUserService {

    private final ViolationToSquadUserRepository violationToSquadUserRepository;

    @Override
    public List<ViolationToSquadUser> getAll() {
        List<ViolationToSquadUser> violationsToSquadUsers = violationToSquadUserRepository.findAll();

        violationsToSquadUsers.sort(
                Comparator
                        .comparing(ViolationToSquadUser::getDate)
                        .thenComparing(vTSU -> vTSU.getViolator().getSquad().getName())
                        .thenComparing(vTSU -> vTSU.getViolator().getFullName(), Comparator.reverseOrder()));

        return violationsToSquadUsers;
    }

    @Override
    public List<ViolationToSquadUser> getAllByIsSolved(boolean isSolved) {
        List<ViolationToSquadUser> violationsToSquadUsers = violationToSquadUserRepository
                .findAllByIsSolved(isSolved);
        violationsToSquadUsers.sort(
                Comparator
                        .comparing(ViolationToSquadUser::getDate, Comparator.reverseOrder())
                        .thenComparing(ViolationToSquadUser::getViolation)
                        .thenComparing(
                                vTSU -> vTSU.getViolator().getSquad() != null
                                        ? vTSU.getViolator().getSquad().getName()
                                        : null, Comparator.nullsLast(String::compareTo))
                        .thenComparing(vTSU -> vTSU.getViolator().getFullName())
        );

        return violationsToSquadUsers;
    }

    @Override
    public List<ViolationToSquadUser> getAllUnsolvedViolationsBySquad(Squad squad) {
        return violationToSquadUserRepository.findAllByViolator_SquadAndIsSolved(squad, false);
    }

    @Override
    public List<ViolationToSquadUser> getAllUnsolvedViolationsBySquadUser(SquadUser squadUser) {
        return violationToSquadUserRepository.findAllByViolatorAndIsSolved(squadUser, false);
    }
}
