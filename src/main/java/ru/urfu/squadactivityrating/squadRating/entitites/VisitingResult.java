package ru.urfu.squadactivityrating.squadRating.entitites;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;

import java.util.List;

/**
 * Класс, хранящий баллы за результаты участия в мероприятиях по типам
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "visits")
@Entity
@Table(name = "visiting_results")
public class VisitingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VisitingResults visitingResult;

    private Double weightInSection1;
    private Double weightInSection2;
    private Double weightInSection5;
    private Double weightInSection6;

    @OneToMany(mappedBy = "visitingResult",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private List<EventToSquadUser> visits;
}
