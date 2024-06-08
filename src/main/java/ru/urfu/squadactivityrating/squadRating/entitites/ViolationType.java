package ru.urfu.squadactivityrating.squadRating.entitites;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.ViolationTypes;

import java.util.List;

/**
 * Сущность типа дисциплинарного нарушения
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "violation_type")
public class ViolationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double weight;

    @Enumerated(EnumType.STRING)
    private ViolationTypes violation;

    @OneToMany(mappedBy = "violationType",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private List<Violation> violations;
}
