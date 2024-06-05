package ru.urfu.squadactivityrating.squadRating.entitites;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "weight_rating_sections")
public class WeightRatingSections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventTypes eventTypes;

    private Integer weightRatingSection;
}
