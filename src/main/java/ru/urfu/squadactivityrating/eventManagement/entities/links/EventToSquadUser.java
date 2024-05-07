package ru.urfu.squadactivityrating.eventManagement.entities.links;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

/**
 * Сущность посещения событий участниками
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_to_squad_user")
public class EventToSquadUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "squad_user_id")
    private SquadUser squadUser;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "event_id")
    private Event event;
}
