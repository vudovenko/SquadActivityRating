package ru.urfu.squadactivityrating.eventManagement.contollers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.EventType;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;

import java.util.*;
import java.time.Duration;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с событиями
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final SquadUserService squadUserService;
    private final EventToSquadUserService eventToSquadUserService;

    /**
     * Метод для отображения страницы списка событий
     *
     * @param type  тип события
     * @param model модель
     * @return страница списка событий
     */
    @GetMapping
    public String getEventListPage(@RequestParam(name = "type", required = false) String type,
                                   Model model) {
        model.addAttribute("events",
                eventService.getEventsByType(type == null
                        ? EventTypes.SPORT
                        : EventTypes.valueOf(type.toUpperCase())));
        return "eventManagement/events";
    }

    @GetMapping("/create")
    public String getCreateEventPage(Model model) {
        Event event = new Event();
        EventType eventType = new EventType();
        eventType.setEventTypeValue(EventTypes.SPORT);
        event.setEventType(eventType);
        List<SquadUser> fighters = squadUserService.getFighters();
        Map<Boolean, List<SquadUser>> fightersMap = fighters
                .stream()
                .collect(Collectors.groupingBy(f -> false));

        model.addAttribute("fighters", fightersMap);
        model.addAttribute("event", event);
        // todo выделить во фрагмент этот view
        return "eventManagement/create_or_update_event";
    }

    @GetMapping("/{eventId}/update")
    public String getUpdateEventPage(@PathVariable Long eventId, Model model) {
        Event event = eventService.getEventById(eventId);
        List<SquadUser> fighters = squadUserService.getFighters();
        Map<Boolean, List<SquadUser>> fightersMap = fighters
                .stream()
                .collect(Collectors.groupingBy((SquadUser f) -> f.getEvents().contains(event),
                        () -> new TreeMap<>(Comparator.reverseOrder()),
                        Collectors.toList()));
        model.addAttribute("event", event);
        model.addAttribute("fighters", fightersMap);
        // todo выделить во фрагмент этот view
        return "eventManagement/create_or_update_event";
    }

    @PostMapping
    public String createEvent(Event event,
                              @RequestParam("hoursDuration")
                              Integer hoursDuration, // todo указать дефолтные значения
                              @RequestParam("minutesDuration")
                              Integer minutesDuration,
                              @RequestParam("eventTypeValue")
                              String eventType,
                              @RequestParam(name = "selectedFightersIds", required = false)
                              Long... selectedFightersIds) {
        event.setDuration(Duration.ofHours(hoursDuration).plusMinutes(minutesDuration));
        EventType eventTypeObj = new EventType();
        EventTypes eventTypes = EventTypes.valueOf(eventType);
        eventTypeObj.setEventTypeValue(eventTypes);
        event.setEventType(eventTypeObj);
        List<SquadUser> selectedFighters = squadUserService.getUsersByIds(selectedFightersIds);
        event.setParticipants(selectedFighters);

        Event eventEntity = eventService.saveEvent(event);

        return "redirect:/events/" + eventEntity.getId();
    }

    /**
     * Метод для отображения карточки события
     *
     * @param id    идентификатор события
     * @param model модель
     * @return страница с карточкой события
     */
    @GetMapping("/{id}")
    public String getEventCard(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        return "eventManagement/event_card";
    }

    /**
     * Метод для удаления события
     *
     * @param id идентификатор удаляемого события
     * @return страница со списком событий
     */
    @GetMapping("/{id}/delete")
    public String deleteSquad(@PathVariable Long id) {
        eventService.deleteEvent(id);

        return "redirect:/events";
    }
}
