package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.usersRating.generalRating.services.GeneralRatingService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Контроллер для управления отрядами
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/squads")
public class SquadController {

    private final SquadService squadService;
    private final SquadUserService squadUserService;
    private final GeneralRatingService generalRatingService;

    /**
     * Метод для отображения страницы списка отрядов
     *
     * @param model модель
     * @return страница списка отрядов
     */
    @GetMapping
    public String getSquadListPage(Model model) {
        model.addAttribute("squads", squadService.getAllSquads());
        return "squadManagement/squads";
    }

    /**
     * Метод для отображения страницы создания нового отряда
     *
     * @param model модель
     * @return страница создания нового отряда
     */
    @GetMapping("/create")
    public String getCreateSquadPage(Model model) {
        List<SquadUser> fighters = squadUserService.getFreeFighters();
        // todo по-хорошему надо ключ и значение поменять местами
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

    /**
     * Метод для отображения страницы редактирования отряда
     *
     * @param squadId идентификатор редактируемого отряда
     * @param model   модель
     * @return страница редактирования отряда
     */
    @GetMapping("/{squadId}/update")
    public String getUpdateSquadPage(@PathVariable Long squadId, Model model) {
        List<SquadUser> squadFighters = squadUserService.getSquadFighters(squadId);
        List<SquadUser> fighters = squadUserService.getFreeFighters();
        fighters.addAll(0, squadFighters);

        // todo по-хорошему надо ключ и значение поменять местами
        Map<SquadUser, Boolean> fightersMap = fighters
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        fighter -> fighter.getSquad() != null,
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new));
        model.addAttribute("fighters", fightersMap);

        List<SquadUser> commanders = squadUserService.getFreeCommanders();
        SquadUser currentCommander = squadService.getSquadById(squadId).getCommander();
        if (currentCommander != null) {
            commanders.add(0, currentCommander);
        }
        model.addAttribute("commanders", commanders);
        Squad squad = squadService.getSquadById(squadId);
        model.addAttribute("squad", squad);

        return "squadManagement/create-or-update-squad";
    }

    /**
     * Метод для обновления отряда
     *
     * @param squadId             идентификатор редактируемого отряда
     * @param squad               обновляемый отряд
     * @param selectedFightersIds идентификаторы добавляемых в отряд бойцов
     * @return страница с карточкой отредактированного отряда
     */
    @PostMapping("/{squadId}/update")
    public String updateSquad(@PathVariable Long squadId,
                              Squad squad,
                              Long... selectedFightersIds) {
        squadService.updateSquad(squadId, squad, selectedFightersIds);

        return "redirect:/squads/" + squadId;
    }

    /**
     * Метод для создания отряда
     *
     * @param squad               создаваемый отряд
     * @param selectedFightersIds идентификаторы добавляемых в отряд бойцов
     * @return страница с карточкой созданного отряда
     */
    @PostMapping
    public String createSquad(Squad squad,
                              @RequestParam(name = "selectedFightersIds", required = false)
                              Long... selectedFightersIds) {
        List<SquadUser> selectedFighters = squadUserService.getUsersByIds(selectedFightersIds);
        selectedFighters.forEach(f -> f.setSquad(squad));
        Squad squadEntity = squadService.saveSquad(squad);

        return "redirect:/squads/" + squadEntity.getId();
    }

    /**
     * Метод для удаления отряда
     *
     * @param id идентификатор удаляемого отряда
     * @return страница со списком отрядов
     */
    @GetMapping("/{id}/delete")
    public String deleteSquad(@PathVariable Long id) {
        squadService.deleteSquad(id);

        return "redirect:/squads";
    }

    /**
     * Метод для отображения карточки отряда
     *
     * @param id    идентификатор отряда
     * @param model модель
     * @return страница с карточкой отряда
     */
    @GetMapping("/{id}")
    public String getSquadCard(@PathVariable Long id, Model model) {
        Squad squad = squadService.getSquadById(id);
        model.addAttribute("squad", squad);
        List<SquadUser> allUsersInSquad = squadUserService.getAllBySquad(squad);
        List<Pair<Integer, Pair<SquadUser, Double>>> squadUsersToTotalScores
                = generalRatingService.getUsersToResults(allUsersInSquad);
        model.addAttribute("squadUsersToTotalScores", squadUsersToTotalScores);
        return "squadManagement/squad_card";
    }
}
