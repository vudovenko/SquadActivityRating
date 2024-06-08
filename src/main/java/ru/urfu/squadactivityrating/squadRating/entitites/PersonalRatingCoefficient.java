package ru.urfu.squadactivityrating.squadRating.entitites;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;


/**
 * Сущность для хранения коэффициентов личного рейтинга бойца
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personal_rating_coefficients")
public class PersonalRatingCoefficient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventTypes eventTypes;

    private Double personalRatingCoefficient;
}
