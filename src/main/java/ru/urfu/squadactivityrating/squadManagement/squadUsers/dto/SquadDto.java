package ru.urfu.squadactivityrating.squadManagement.squadUsers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SquadDto {

    private final Squad squad;
    private final Map<SquadUser, Boolean> usersMap;
}
