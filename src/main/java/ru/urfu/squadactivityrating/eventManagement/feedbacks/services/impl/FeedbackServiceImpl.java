package ru.urfu.squadactivityrating.eventManagement.feedbacks.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.repositories.FeedbackRepository;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.services.FeedbackService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getAllFeedbacksByEventId(Long eventId) {
        return feedbackRepository.findByEventToSquadUser_Event_id(eventId);
    }
}
