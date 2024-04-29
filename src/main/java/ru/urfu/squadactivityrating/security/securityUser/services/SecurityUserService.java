package ru.urfu.squadactivityrating.security.securityUser.services;

import ru.urfu.squadactivityrating.security.securityUser.entities.SecurityUser;

public interface SecurityUserService {

    SecurityUser getUserByEmail(String email);

    SecurityUser saveUser(SecurityUser securityUser);
}
