package ru.urfu.squadactivityrating.security.securityUser.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.security.securityUser.entities.SecurityUser;
import ru.urfu.squadactivityrating.security.securityUser.repositories.SecurityUserRepository;
import ru.urfu.squadactivityrating.security.securityUser.services.SecurityUserService;

@Service
@AllArgsConstructor
public class SecurityUserServiceImpl implements SecurityUserService {

    private final SecurityUserRepository securityUserRepository;

    @Override
    public SecurityUser getUserByEmail(String email) {
        return securityUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public SecurityUser saveUser(SecurityUser securityUser) {
        return securityUserRepository.save(securityUser);
    }
}

