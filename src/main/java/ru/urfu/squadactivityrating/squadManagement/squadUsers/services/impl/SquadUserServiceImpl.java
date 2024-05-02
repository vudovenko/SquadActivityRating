package ru.urfu.squadactivityrating.squadManagement.squadUsers.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.security.securityUsers.enums.UserRole;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.repositories.SquadUserRepository;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.List;

@Service
@AllArgsConstructor
public class SquadUserServiceImpl implements SquadUserService {

    private final SquadUserRepository squadUserRepository;

    @Override
    public SquadUser saveUser(SquadUser squadUser) {
        return squadUserRepository.save(squadUser);
    }

    @Override
    public List<SquadUser> getFreeCommanders() {
        return squadUserRepository.getNonCommanders(UserRole.COMMANDER);
    }

    @Override
    public List<SquadUser> getFighters() {
        return squadUserRepository.findByRole(UserRole.FIGHTER);
    }

    @Override
    public SquadUser getUserById(Long id) {
        return squadUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<SquadUser> getUsersByIds(Long[] ids) {
        return squadUserRepository.findByIds(ids);
    }
}
