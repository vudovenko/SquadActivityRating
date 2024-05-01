package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.dto.SquadDto;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squads")
public class SquadControllers {

    private final SquadService squadService;
    private final SquadUserService squadUserService;

    @GetMapping
    public String getSquadListPage(Model model) {
        model.addAttribute("squads", squadService.getAllSquads());

        return "squadManagement/squads";
    }

    @GetMapping("/create")
    public String getCreateSquadPage(Model model) {
        List<SquadUser> fighters = squadUserService.getFighters(); // todo вынести в сервис
        Map<SquadUser, Boolean> fightersMap = fighters
                .stream()
                .filter(f -> f.getSquad() == null)
                .collect(Collectors.toMap(Function.identity(), f -> false));
        model.addAttribute("squadAndUsers", new SquadDto(new Squad(), fightersMap));
        model.addAttribute("commanders",
                squadUserService.getFreeCommanders());

        return "squadManagement/create-squad";
    }

    @PostMapping
    public String createSquad(SquadDto squadDto, Long... selectedFightersIds) {
        List<SquadUser> selectedFighters = squadUserService.getUsersByIds(selectedFightersIds);
        Squad squad = squadDto.getSquad();
        selectedFighters.forEach(f -> f.setSquad(squad));
        squadService.saveSquad(squad);

        return "redirect:/squads"; // todo потом сменить путь на карточку отряда
    }

    @GetMapping("/{id}/delete")
    public String deleteSquad(@PathVariable String id) {
        squadService.deleteSquad(Long.valueOf(id));

        return "redirect:/squads";
    }
}
