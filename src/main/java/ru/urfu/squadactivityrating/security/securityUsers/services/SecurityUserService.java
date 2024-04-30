package ru.urfu.squadactivityrating.security.securityUsers.services;

import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;

public interface SecurityUserService {

    SecurityUser getUserByLogin(String login);

    SecurityUser saveUser(SecurityUser securityUser);
}
