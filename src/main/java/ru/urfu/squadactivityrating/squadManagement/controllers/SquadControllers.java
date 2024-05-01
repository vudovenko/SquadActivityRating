package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

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
        model.addAttribute("squad", new Squad());
        model.addAttribute("commanders",
                squadUserService.getFreeCommanders());

        return "squadManagement/create-squad";
    }

    @PostMapping
    public String createSquad(Squad squad) {
        squadService.createSquad(squad);

        return "redirect:/squads"; // todo потом сменить путь на карточку отряда
    }

    @GetMapping("/{id}/delete")
    public String deleteSquad(@PathVariable String id) {
        squadService.deleteSquad(Long.valueOf(id));

        return "redirect:/squads";
    }
}
