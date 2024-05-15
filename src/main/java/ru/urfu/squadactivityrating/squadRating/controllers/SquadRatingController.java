package ru.urfu.squadactivityrating.squadRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squad-rating")
public class SquadRatingController {

    private final EventToSquadUserService eventToSquadUserService;

    @GetMapping
    public String getSquadRatingPage() {
        return "squadRating/squad_rating";
    }

    @GetMapping("/sports")
    public String getSportsSquadRatingPage(Model model) {

        // todo перенести в сервис
        List<EventToSquadUser> eventsToSquadUsersByEventType = eventToSquadUserService
                .getEventsToSquadUsersByEventType(EventTypes.SPORT);
        LinkedHashSet<Event> events =
                new LinkedHashSet<>(eventsToSquadUsersByEventType
                        .stream()
                        .map(EventToSquadUser::getEvent)
                        .sorted(Comparator.comparing(Event::getDate))
                        .toList());
        model.addAttribute("events", events);
        LinkedHashMap<Squad, LinkedHashMap<Event, List<VisitingResult>>> squadVisitingResults
                = new LinkedHashMap<>();

        // Надо заполнить каждый отряд значениями мероприятий. Т.е. будет ключ отряда, которому будут соответствовать мероприятия. Они будут добавляться, даже если в мероприятии никто не участвовал.
        for (EventToSquadUser eventToSquadUser : eventsToSquadUsersByEventType) {
            Squad squad = eventToSquadUser.getSquadUser().getSquad();
            if (!squadVisitingResults.containsKey(squad)) {
                squadVisitingResults.put(squad, new LinkedHashMap<>());
            }
            for (Event event : events) {
                if (!squadVisitingResults.get(squad).containsKey(event)) {
                    squadVisitingResults.get(squad).put(event, new ArrayList<>());
                }
            }
        }

        for (EventToSquadUser eventToSquadUser : eventsToSquadUsersByEventType) {
            Squad squad = eventToSquadUser.getSquadUser().getSquad();
            squadVisitingResults
                    .get(squad)
                    .get(eventToSquadUser.getEvent())
                    .add(eventToSquadUser.getVisitingResult());
        }

        model.addAttribute("squadVisitingResults", squadVisitingResults);
        return "squadRating/sports";
    }
}
