package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squads")
public class SquadControllers {

    private final SquadService squadService;

    @GetMapping
    public String getSquadListPage(Model model) {
        model.addAttribute("squads", squadService.getAllSquads());

        return "squadManagement/squads";
    }

    @GetMapping("/{id}/delete")
    public String deleteSquad(@PathVariable String id) {
        squadService.deleteSquad(Long.valueOf(id));

        return "redirect:/squads";
    }
}
