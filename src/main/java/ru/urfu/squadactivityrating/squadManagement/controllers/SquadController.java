package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squads")
public class SquadController {

    private final SquadService squadService;
    private final SquadUserService squadUserService;

    @GetMapping
    public String getSquadListPage(Model model) {
        model.addAttribute("squads", squadService.getAllSquads());
        return "squadManagement/squads";
    }

    @GetMapping("/create")
    public String getCreateSquadPage(Model model) {
        List<SquadUser> fighters = squadUserService.getFighters(); // todo заменить на getFreeFighters
        Map<SquadUser, Boolean> fightersMap = fighters
                .stream()
                .filter(f -> f.getSquad() == null)
                .collect(Collectors.toMap(Function.identity(), f -> false));
        model.addAttribute("fighters", fightersMap);
        model.addAttribute("commanders",
                squadUserService.getFreeCommanders());
        model.addAttribute("squad", new Squad());

        return "squadManagement/create-or-update-squad";
    }

    @PostMapping
    public String createSquad(Squad squad, Long... selectedFightersIds) {
        List<SquadUser> selectedFighters = squadUserService.getUsersByIds(selectedFightersIds);
        selectedFighters.forEach(f -> f.setSquad(squad));
        squadService.saveSquad(squad);

        return "redirect:/squads";
    }

    @GetMapping("/{id}/delete")
    public String deleteSquad(@PathVariable Long id) {
        squadService.deleteSquad(id);

        return "redirect:/squads";
    }

    @GetMapping("/{id}")
    public String getSquadCard(@PathVariable Long id, Model model) {
        model.addAttribute("squad", squadService.getSquadById(id));
        return "squadManagement/squad_card";
    }
}
