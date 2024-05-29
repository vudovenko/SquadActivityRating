package ru.urfu.squadactivityrating.eventManagement.entities.links;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.feedbacks.entities.Feedback;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;

/**
 * Сущность посещения событий участниками
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_to_squad_user")
public class EventToSquadUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "eventToSquadUser",
            cascade = CascadeType.ALL)
    private Feedback feedback;


    @OneToOne(mappedBy = "eventToSquadUser",
            cascade = CascadeType.ALL)
    private VisitingHours visitingHours;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "squad_user_id")
    private SquadUser squadUser;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "visiting_result_id")
    private VisitingResult visitingResult;

    @Override
    public String toString() {
        return "EventToSquadUser{" +
                "id=" + id +
                ", squadUser=" + squadUser.getFullName() +
                ", event=" + event +
                ", feedback="
                + (feedback != null ?
                feedback.getRating() + "/" + feedback.getComment()
                : "no feedback") +
                '}';
    }
}
