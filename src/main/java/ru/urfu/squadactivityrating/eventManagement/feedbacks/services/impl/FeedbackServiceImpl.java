package ru.urfu.squadactivityrating.eventManagement.feedbacks.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.repositories.FeedbackRepository;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.services.FeedbackService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EventToSquadUserService eventToSquadUserService;

    @Override
    public List<Feedback> getAllFeedbacksByEventId(Long eventId) {
        return feedbackRepository.findByEventToSquadUser_Event_id(eventId);
    }

    @Override
    public void leaveFeedback(Long eventId, Long squadUserId, String comment, int rating) {
        EventToSquadUser eventToSquadUser = eventToSquadUserService.
                getEventToSquadUserByEventIdAndSquadUserId(eventId, squadUserId);
        Optional<Feedback> feedbackOptional =
                feedbackRepository.findByEventToSquadUser(eventToSquadUser);
        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            feedback.setComment(comment);
            feedback.setRating(rating);
            feedbackRepository.save(feedback);
        } else {
            Feedback feedback = Feedback
                    .builder()
                    .eventToSquadUser(eventToSquadUser)
                    .comment(comment)
                    .rating(rating)
                    .build();
            feedbackRepository.save(feedback);
        }
    }
}
