package ru.urfu.squadactivityrating.squadRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squad-rating")
public class SquadRatingController {

    private EventService eventService;

    @GetMapping
    public String getSquadRatingPage() {
        return "squadRating/squad_rating";
    }

    @GetMapping("/sports")
    public String getSportsSquadRatingPage(Model model) {
        model.addAttribute("events",
                eventService.getEventsByType(EventTypes.SPORT));
        return "squadRating/sports";
    }
}
