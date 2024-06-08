package ru.urfu.squadactivityrating.squadRating.entitites.links;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.Violation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Сущность для связи между дисциплинарными нарушениями и бойцами
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "violation_to_squad_user")
public class ViolationToSquadUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isSolved;
    private String comment;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "violator_id")
    private SquadUser violator;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "violation_id")
    private Violation violation;

    public String getFormattedDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTimeFormatter.format(date);
    }
}
