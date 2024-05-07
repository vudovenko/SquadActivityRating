package ru.urfu.squadactivityrating.eventManagement.feedbacks.dto;

import lombok.Data;

/**
 * DTO для отзыва
 */
@Data
public class FeedbackDTO {

    private int rating;
    private String comment;
}
