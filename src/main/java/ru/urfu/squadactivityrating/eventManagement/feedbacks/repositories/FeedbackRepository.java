package ru.urfu.squadactivityrating.eventManagement.feedbacks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с отзывами
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Метод для получения отзывов по идентификатору посещения
     *
     * @param eventToSquadUserId идентификатор посещения
     * @return список отзывов
     */
    List<Feedback> findByEventToSquadUser_Event_id(Long eventToSquadUserId);

    /**
     * Метод для получения отзыва по посещению
     *
     * @param eventToSquadUser посещение
     * @return отзыв
     */
    Optional<Feedback> findByEventToSquadUser(EventToSquadUser eventToSquadUser);
}
