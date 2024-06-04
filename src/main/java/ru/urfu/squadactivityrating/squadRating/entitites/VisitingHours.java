package ru.urfu.squadactivityrating.squadRating.entitites;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;

import java.time.LocalDateTime;

/**
 * Класс для хранения времени посещения мероприятий
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visiting_hours")
public class VisitingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "event_to_squad_user_id")
    private EventToSquadUser eventToSquadUser;
}
