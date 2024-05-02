package ru.urfu.squadactivityrating.squadManagement.squadUsers.services;

import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.util.List;

public interface SquadUserService {

    SquadUser saveUser(SquadUser squadUser);

    List<SquadUser> getFreeCommanders();

    List<SquadUser> getFighters();

    SquadUser getUserById(Long id);

    List<SquadUser> getUsersByIds(Long[] ids);

    List<SquadUser> getSquadFighters(Long squadId);

    List<SquadUser> getFreeFighters();
}
