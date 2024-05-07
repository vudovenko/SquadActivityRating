package ru.urfu.squadactivityrating.eventManagement.feedbacks.services;

import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;

import java.util.List;

public interface FeedbackService {

    List<Feedback> getAllFeedbacksByEventId(Long eventId);
}
