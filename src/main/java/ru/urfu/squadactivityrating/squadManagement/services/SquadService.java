package ru.urfu.squadactivityrating.squadManagement.services;

import ru.urfu.squadactivityrating.squadManagement.entities.Squad;

import java.util.List;

public interface SquadService {

    List<Squad> getAllSquads();

    void deleteSquad(Long squadId);

    Squad saveSquad(Squad squad);

    Squad getSquadById(Long squadId);

    void updateSquad(Long squadId,
                     Squad squadDto,
                     Long[] selectedFightersIds);
}
