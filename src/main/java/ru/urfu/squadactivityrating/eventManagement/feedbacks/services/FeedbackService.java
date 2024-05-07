package ru.urfu.squadactivityrating.eventManagement.feedbacks.services;

import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;

import java.util.List;

/**
 * Сервис для работы с отзывами
 */
public interface FeedbackService {

    /**
     * Метод для получения отзывов по идентификатору события
     *
     * @param eventId идентификатор события
     * @return список отзывов
     */
    List<Feedback> getAllFeedbacksByEventId(Long eventId);

    /**
     * Метод для добавления отзыва по идентификатору события и идентификатору пользователя
     *
     * @param eventId     идентификатор события
     * @param squadUserId идентификатор пользователя
     * @param comment     комментарий
     * @param rating      оценка
     */
    void leaveFeedback(Long eventId, Long squadUserId, String comment, int rating);
}
