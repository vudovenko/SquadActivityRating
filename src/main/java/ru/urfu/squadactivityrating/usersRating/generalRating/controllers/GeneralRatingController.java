package ru.urfu.squadactivityrating.usersRating.generalRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.usersRating.generalRating.services.GeneralRatingService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/general-rating")
public class GeneralRatingController {

    private final GeneralRatingService generalRatingService;
    private final SquadUserService squadUserService;

    @GetMapping
    public String getGeneralRatingPage(Model model) {
        List<Pair<Integer, Pair<SquadUser, Double>>> usersToResults
                = generalRatingService.getUsersToResults(squadUserService.getAllSquadUsers());
        model.addAttribute("usersToResults", usersToResults);

        return "usersRating/generalRating/general_rating";
    }
}
