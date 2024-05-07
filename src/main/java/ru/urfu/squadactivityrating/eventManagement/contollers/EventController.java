package ru.urfu.squadactivityrating.eventManagement.contollers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;

/**
 * Контроллер для работы с событиями
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

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
                        ? EventType.SPORT
                        : EventType.valueOf(type.toUpperCase())));
        return "eventManagement/events";
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
