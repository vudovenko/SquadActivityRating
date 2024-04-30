package ru.urfu.squadactivityrating.security.securityUsers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;

import java.util.Optional;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, Long> {

    Optional<SecurityUser> findByLogin(String login);
}
