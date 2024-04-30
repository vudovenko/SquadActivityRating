package ru.urfu.squadactivityrating.security.securityUsers.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;
import ru.urfu.squadactivityrating.security.securityUsers.repositories.SecurityUserRepository;
import ru.urfu.squadactivityrating.security.securityUsers.services.SecurityUserService;

@Service
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements SecurityUserService {

    private final SecurityUserRepository securityUserRepository;

    @Override
    public SecurityUser getUserByLogin(String login) {
        return securityUserRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public SecurityUser saveUser(SecurityUser securityUser) {
        return securityUserRepository.save(securityUser);
    }
}

