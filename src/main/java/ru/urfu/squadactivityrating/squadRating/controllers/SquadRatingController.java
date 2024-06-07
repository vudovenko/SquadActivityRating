package ru.urfu.squadactivityrating.squadRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;

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
    public String getSportsSquadRatingPage(@RequestParam(name = "eventType", required = false) String eventType,
                                           Model model) {
        // todo добавить логику обработки мероприятий 3, 4 и 8 типа
        if (eventType == null) {
            return "squadRating/squad_rating";
        }
        EventTypes eventTypes = EventTypes.valueOf(eventType.toUpperCase());
        visitingResultService.setVisitingResultsInModel(
                eventTypes,
                model);
        if (eventTypes == EventTypes.SPORT
                || eventTypes == EventTypes.CREATIVE_WORK
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU) {
            return "squadRating/visiting_results1256";
        } else if (eventTypes == EventTypes.SOCIAL_WORK
                || eventTypes == EventTypes.PRODUCTION_WORK) {
            return "squadRating/visiting_results34";
        }

        return "squadRating/squad_rating";
    }
}
