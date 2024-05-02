package ru.urfu.squadactivityrating.squadManagement.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.repositories.SquadRepository;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.dto.SquadDto;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SquadServiceImpl implements SquadService {

    private final SquadRepository squadRepository;
    private final SquadUserService squadUserService;

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

    @Override
    public Squad saveSquad(Squad squad) {
        return squadRepository.save(squad);
    }

    @Override
    public Squad getSquadById(Long id) {
        return squadRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Squad not found"));
    }

    @Override
    public void saveOrUpdateSquad(Long squadId,
                                  Squad squadDto,
                                  Long[] selectedFightersIds) {
        List<SquadUser> selectedFighters = squadUserService.getUsersByIds(selectedFightersIds);
        List<SquadUser> oldFighters = squadUserService.getSquadFighters(squadId);
        oldFighters.forEach(f -> {
            f.setSquad(null);
        });
//        selectedFighters.forEach(f -> f.setSquad(squadDto));
//        squadRepository.save(squadDto);


//        if (squadId != null) {
//            Optional<Squad> squadOptional = squadRepository.findById(Long.parseLong(squadId));
//            if (squadOptional.isPresent()) {
//                Squad squad = squadOptional.get();
//
//
//                squad.getUsers().forEach(f -> f.setSquad(null));
//                selectedFighters.forEach(f -> f.setSquad(squad));
//                squad.setName(squadDto.getName());
//                squad.setDescription(squadDto.getDescription());
//                squad.setCommander(squadDto.getCommander());
//                squadDto.setName("!!!");
//                squadRepository.save(squadDto);
//            } else {
//                throw new IllegalArgumentException("Squad not found");
//            }
//        } else {
//            throw new IllegalArgumentException("Squad not found");
//        }
    }
}
