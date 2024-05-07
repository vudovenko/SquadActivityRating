package ru.urfu.squadactivityrating.eventManagement.feedbacks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.dto.FeedbackDTO;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.services.FeedbackService;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;

/**
 * Контроллер для работы с отзывами о событиях
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/events/{id}/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final EventService eventService;

    /**
     * Метод для получения страницы с отзывами
     *
     * @param eventId идентификатор события
     * @param model   модель
     * @return страница с отзывами
     */
    @GetMapping
    public String getFeedbackListPage(@PathVariable(name = "id") Long eventId,
                                      Model model) {
        model.addAttribute("event", eventService.getEventById(eventId));
        model.addAttribute("feedbacks",
                feedbackService.getAllFeedbacksByEventId(eventId));
        return "eventManagement/feedbacks/feedbacks";
    }

    /**
     * Метод для получения формы для оставления отзыва
     *
     * @param eventId идентификатор события
     * @param model   модель
     * @return страница с формой
     */
    /*todo проверить, что отзыв оставляется после того,
       как событие пройдет и что пользователь есть в участниках*/
    @GetMapping("/leave-feedback")
    public String getFeedbackFormPage(@PathVariable(name = "id") Long eventId,
                                      Model model) {
        model.addAttribute("event", eventService.getEventById(eventId));
        model.addAttribute("feedbackDTO", new FeedbackDTO());
        return "eventManagement/feedbacks/feedback_form";
    }

    /**
     * Метод для сохранения данных отзыва
     *
     * @param eventId     идентификатор события
     * @param currentUser текущий пользователь
     * @param feedbackDTO DTO с данными с формы отзыва
     * @return страница с отзывами
     */
    @PostMapping("/leave-feedback")
    public String leaveFeedback(@PathVariable(name = "id") Long eventId,
                                @AuthenticationPrincipal SecurityUser currentUser,
                                FeedbackDTO feedbackDTO) {
        feedbackService.leaveFeedback(eventId,
                currentUser.getId(),
                feedbackDTO.getComment(),
                feedbackDTO.getRating());
        return "redirect:/events/" + eventId + "/feedbacks";
    }
}
