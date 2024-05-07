package ru.urfu.squadactivityrating.eventManagement.feedbacks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByEventToSquadUser_Event_id(Long eventToSquadUserId);
}
