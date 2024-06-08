package ru.urfu.squadactivityrating.squadRating.entitites;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность дисциплинарного нарушения
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "violations")
public class Violation implements Comparable<Violation> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "type_id")
    private ViolationType violationType;

    @Override
    public int compareTo(Violation o) {
        int compareResult = this.violationType.getWeight()
                .compareTo(o.violationType.getWeight());
        if (compareResult == 0) {
            return this.name.compareTo(o.name);
        }
        return compareResult;
    }
}
