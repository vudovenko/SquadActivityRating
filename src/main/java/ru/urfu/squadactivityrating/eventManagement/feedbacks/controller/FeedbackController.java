package ru.urfu.squadactivityrating.eventManagement.feedbacks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.services.FeedbackService;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events/{id}/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final EventService eventService;

    @GetMapping
    public String getFeedbackListPage(@PathVariable(name = "id") Long eventId,
                                      Model model) {
        model.addAttribute("event", eventService.getEventById(eventId));
        model.addAttribute("feedbacks",
                feedbackService.getAllFeedbacksByEventId(eventId));
        return "eventManagement/feedbacks/feedbacks";
    }

}
