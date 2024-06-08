package ru.urfu.squadactivityrating.squadRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;
import ru.urfu.squadactivityrating.squadRating.service.ViolationToSquadUserService;

import java.util.List;

/**
 * Контроллер для работы с дисциплинарными нарушениями
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/violations")
public class ViolationsController {

    private final ViolationToSquadUserService violationToSquadUserService;

    /**
     * Метод для отображения страницы дисциплинарных нарушений
     * @param model
     * @return
     */
    @GetMapping
    public String getViolationsPage(Model model) {
        List<ViolationToSquadUser> solvedViolations = violationToSquadUserService.getAllByIsSolved(true);
        List<ViolationToSquadUser> notSolvedViolations = violationToSquadUserService.getAllByIsSolved(false);
        model.addAttribute("solvedViolationsToSquadUsers", solvedViolations);
        model.addAttribute("notSolvedViolationsToSquadUsers", notSolvedViolations);
        return "squadRating/violations";
    }
}
