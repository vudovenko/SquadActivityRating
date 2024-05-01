package ru.urfu.squadactivityrating.squadManagement.squadUsers.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.repositories.SquadUserRepository;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

@Service
@AllArgsConstructor
public class SquadUserServiceImpl implements SquadUserService {

    private final SquadUserRepository squadUserRepository;

    @Override
    public SquadUser saveUser(SquadUser squadUser) {
        return squadUserRepository.save(squadUser);
    }
}
