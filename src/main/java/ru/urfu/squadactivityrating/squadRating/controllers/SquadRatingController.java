package ru.urfu.squadactivityrating.squadRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;
import ru.urfu.squadactivityrating.squadRating.service.impl.VisitingResultServiceImpl;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Контроллер для работы с расчётом рейтинга отрядов
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/squad-ratings")
public class SquadRatingController {

    private final VisitingResultService visitingResultService;

    /**
     * Метод для отображения страницы рейтинга отрядов.
     * <br>
     * В зависимости от параметра <code>eventType</code> отображается главная страница рейтинга
     * или страница с подробными результатами по типу мероприятия
     *
     * @param eventType тип мероприятия <em>(необязательный параметр)</em>
     * @param model     модель
     * @return страница рейтинга отрядов
     */
    @GetMapping
    public String getSquadRatingsPage(@RequestParam(name = "eventType", required = false)
                                      String eventType,
                                      Model model) {
        // todo добавить логику обработки мероприятий 3, 4 и 8 типа
        if (eventType == null) {
            LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> totalSquadVisitingResults
                    = visitingResultService.getTotalVisitingResultsFromModel(model);
            model.addAttribute("totalSquadVisitingResults", totalSquadVisitingResults);
            List<Pair<Double, Integer>> finalPlacesForTotalResult
                    = visitingResultService
                    .getFinalPlacesFromTotalResult(totalSquadVisitingResults);
            model.addAttribute("finalPlacesForTotalResult", finalPlacesForTotalResult);
            return "squadRating/squad_rating";
        }
        EventTypes eventTypes = EventTypes.valueOf(eventType.toUpperCase());

        if (eventTypes == EventTypes.SPORT
                || eventTypes == EventTypes.CREATIVE_WORK
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU) {

            VisitingResultServiceImpl.SectionResult<Pair<List<VisitingResult>, Double>> sectionResult
                    = visitingResultService.getPointsForEventsWithVisitingResults(eventTypes);
            model.addAttribute("sectionResult", sectionResult);

            List<Event> events = visitingResultService.getEvents(sectionResult.points());
            model.addAttribute("events", events);
            model.addAttribute("squads", sectionResult.points().keySet());
            return "squadRating/visiting_results1256";
        } else if (eventTypes == EventTypes.SOCIAL_WORK
                || eventTypes == EventTypes.PRODUCTION_WORK) {
            VisitingResultServiceImpl.SectionResult<Duration> sectionResult
                    = visitingResultService.getPointsForEventsWithVisitingHours(eventTypes);
            model.addAttribute("sectionResult", sectionResult);

            List<Event> events = visitingResultService.getEvents(sectionResult.points());
            model.addAttribute("events", events);
            model.addAttribute("squads", sectionResult.points().keySet());
            return "squadRating/visiting_results34";
        }

        return "squadRating/squad_rating";
    }
}
