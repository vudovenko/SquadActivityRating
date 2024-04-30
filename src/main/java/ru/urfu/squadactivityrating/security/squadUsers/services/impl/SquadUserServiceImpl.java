package ru.urfu.squadactivityrating.security.squadUsers.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.security.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.security.squadUsers.repositories.SquadUserRepository;
import ru.urfu.squadactivityrating.security.squadUsers.services.SquadUserService;

@Service
@AllArgsConstructor
public class SquadUserServiceImpl implements SquadUserService {

    private final SquadUserRepository squadUserRepository;

    @Override
    public SquadUser saveUser(SquadUser squadUser) {
        return squadUserRepository.save(squadUser);
    }
}
