package ru.urfu.squadactivityrating.security.securityUser.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.security.securityUser.entities.SecurityUser;

import java.util.Optional;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, Integer> {

    Optional<SecurityUser> findByEmail(String email);
}
