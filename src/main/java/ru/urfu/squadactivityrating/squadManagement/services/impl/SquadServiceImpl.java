package ru.urfu.squadactivityrating.squadManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.security.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.repositories.SquadRepository;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SquadServiceImpl implements SquadService {

    private final SquadRepository squadRepository;

    @Override
    public List<Squad> getAllSquads() {
        return squadRepository.findAll();
    }

    @Override()
    public void deleteSquad(Long squadId) {
        Optional<Squad> squadOptional = squadRepository.findById(squadId);

        if (squadOptional.isPresent()) {
            Squad squadEntity = squadOptional.get();
            SquadUser commander = squadEntity.getCommander();
            if (commander != null) {
                commander.setSubordinateSquad(null);
            }
            squadEntity.getUsers().forEach(user -> user.setSquad(null));
            squadRepository.delete(squadEntity);
        } else {
            throw new IllegalArgumentException("Squad not found");
        }
    }
}