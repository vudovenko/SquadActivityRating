package ru.urfu.squadactivityrating.eventManagement.feedbacks.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;

/**
 * Сущность отзыва
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String comment;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "event_to_squad_user_id")
    private EventToSquadUser eventToSquadUser;
}
