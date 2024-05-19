package ru.urfu.squadactivityrating.squadRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squad-ratings")
public class SquadRatingController {

    private final VisitingResultService visitingResultService;

    @GetMapping
    public String getSportsSquadRatingPage(@RequestParam(name = "type", required = false) String type,
                                           Model model) {
        // todo добавить логику обработки мероприятий 3, 4 и 8 типа
        if (type == null) {
            return "squadRating/squad_rating";
        }
        visitingResultService.setVisitingResultsInModel(
                EventTypes.valueOf(type.toUpperCase()),
                model);

        return "squadRating/visiting_results";
    }
}
