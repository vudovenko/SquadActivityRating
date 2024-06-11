package ru.urfu.squadactivityrating.personalRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/personal-ratings")
public class PersonalRatingController {

    private final EventToSquadUserService eventToSquadUserService;
    private final EventService eventService;

    @GetMapping("/{eventId}")
    String getParticipationResults(@PathVariable Long eventId, Model model) {
        List<EventToSquadUser> eventToSquadUsers = eventToSquadUserService.getByEventId(eventId);
        model.addAttribute("eventToSquadUsers", eventToSquadUsers);
        model.addAttribute("eventId", eventId);
        model.addAttribute("event", eventService.getEventById(eventId));

        return "personalRating/participation_results";
    }
}
