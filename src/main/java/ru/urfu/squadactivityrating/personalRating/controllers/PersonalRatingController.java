package ru.urfu.squadactivityrating.personalRating.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.personalRating.dto.VisitingResultsDTO;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;
import ru.urfu.squadactivityrating.squadRating.service.VisitingHoursService;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/personal-ratings")
public class PersonalRatingController {

    private final EventToSquadUserService eventToSquadUserService;
    private final EventService eventService;
    private final VisitingResultService visitingResultService;
    private final VisitingHoursService visitingHoursService;

    // todo рефакторинг + перенести большую часть кода в сервис
    @GetMapping("/{eventId}")
    String getParticipationResults(@PathVariable Long eventId, Model model) {
        List<EventToSquadUser> eventToSquadUsers = eventToSquadUserService.getByEventId(eventId);
        model.addAttribute("eventToSquadUsers", eventToSquadUsers);
        model.addAttribute("eventId", eventId);
        Event event = eventService.getEventById(eventId);
        model.addAttribute("event", event);
        EventTypes eventTypes = event.getEventType().getEventTypeValue();
        VisitingResultsDTO visitingResultsDTO;
        if (eventTypes.equals(EventTypes.SOCIAL_WORK)
                || eventTypes.equals(EventTypes.PRODUCTION_WORK)) {
            visitingResultsDTO = new VisitingResultsDTO(
                    event.getDate(),
                    event.getDate().plus(event.getDuration())
            );
            eventToSquadUsers.sort(Comparator.comparing(eTSU -> eTSU.getVisitingHours() != null));
        } else if (eventTypes == EventTypes.SPORT
                || eventTypes == EventTypes.CREATIVE_WORK) {
            visitingResultsDTO = new VisitingResultsDTO(VisitingResults.PARTICIPATION);
            eventToSquadUsers.sort(Comparator.comparing(eTSU -> eTSU.getVisitingResult() != null));
        } else { // (eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
            // ||eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU){
            if (event.getIsItOnlyParticipation()) {
                visitingResultsDTO = new VisitingResultsDTO(VisitingResults.PRESENCE);
            } else {
                visitingResultsDTO = new VisitingResultsDTO(VisitingResults.PARTICIPATION);
            }
            eventToSquadUsers.sort(Comparator.comparing(eTSU -> eTSU.getVisitingResult() != null));
        }
        model.addAttribute("visitingResultsObject", visitingResultsDTO);

        return "personalRating/participation_results";
    }

    @GetMapping("/{eventToSquadUserId}/clear")
    public String getCreatePersonalRatingPage(@PathVariable Long eventToSquadUserId) {
        EventToSquadUser eventToSquadUser = eventToSquadUserService.getById(eventToSquadUserId);
        EventTypes eventType = eventToSquadUser.getEvent().getEventType().getEventTypeValue();
        VisitingHours visitingHours = eventToSquadUser.getVisitingHours();
        eventToSquadUser.setVisitingHours(null);
        eventToSquadUser.setVisitingResult(null);
        if (visitingHours != null) {
            visitingHoursService.deleteVisitingHours(visitingHours);
        }

        eventToSquadUserService.save(eventToSquadUser);

        return "redirect:/personal-ratings/" + eventToSquadUser.getEvent().getId();
    }

    @PostMapping("/{eventToSquadUserId}/change")
    public String changePersonalRating(@PathVariable Long eventToSquadUserId,
                                       VisitingResultsDTO visitingResultsDTO) {
        EventToSquadUser eventToSquadUser = eventToSquadUserService.getById(eventToSquadUserId);
        EventTypes eventTypes = eventToSquadUser.getEvent().getEventType().getEventTypeValue();
        if (eventTypes != EventTypes.SOCIAL_WORK
                && eventTypes != EventTypes.PRODUCTION_WORK) {
            VisitingResult visitingResult;
            if (eventToSquadUser.getEvent().getIsItOnlyParticipation()) {
                visitingResult = visitingResultService.findByType(VisitingResults.PRESENCE);
            } else {
                visitingResult
                        = visitingResultService.findByType(visitingResultsDTO.getVisitingResult());
            }

            eventToSquadUser.setVisitingResult(visitingResult);
        } else {
            VisitingHours visitingHours = new VisitingHours();
            visitingHours.setStartTime(visitingResultsDTO.getStartTime());
            visitingHours.setEndTime(visitingResultsDTO.getEndTime());
            visitingHours.setEventToSquadUser(eventToSquadUser);
            visitingHours = visitingHoursService.saveVisitingHours(visitingHours);
            eventToSquadUser.setVisitingHours(visitingHours);
        }
        eventToSquadUserService.save(eventToSquadUser);

        return "redirect:/personal-ratings/" + eventToSquadUser.getEvent().getId();
    }

    @PostMapping("/estimate")
    public String clearPersonalRating(VisitingResultsDTO visitingResultsDTO) {
        VisitingResult visitingResult =
                visitingResultService.findByType(visitingResultsDTO.getVisitingResult());
        return "redirect:/personal-ratings/participation-results";
    }
}
