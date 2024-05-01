package ru.urfu.squadactivityrating.squadManagement.squadUsers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.util.List;

import ru.urfu.squadactivityrating.security.securityUsers.enums.UserRole;

public interface SquadUserRepository extends JpaRepository<SquadUser, Long> {

    @Query("""
            from SquadUser squ
            join SecurityUser su
            on squ.securityUser = su
            where :role member of su.roles
            and squ not in (from SquadUser squ1
                              join Squad s1
                              on squ1 = s1.commander)
            """)
    List<SquadUser> getNonCommanders(@Param("role") UserRole role);
}
