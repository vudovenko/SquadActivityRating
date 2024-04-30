package ru.urfu.squadactivityrating.squadManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.repositories.SquadRepository;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SquadServiceImpl implements SquadService {

    private final SquadRepository squadRepository;

    @Override
    public List<Squad> getAllSquads() {
        return squadRepository.findAll();
    }
}
