package ru.urfu.squadactivityrating.squadManagement.squadUsers.services;

import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.util.List;

public interface SquadUserService {

    SquadUser saveUser(SquadUser squadUser);

    List<SquadUser> getFreeCommanders();

    List<SquadUser> getFighters();

    List<SquadUser> getUsersByIds(Long[] ids);
}
