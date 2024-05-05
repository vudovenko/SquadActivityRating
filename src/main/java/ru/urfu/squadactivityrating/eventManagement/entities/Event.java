package ru.urfu.squadactivityrating.eventManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String location;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(columnDefinition = "TIME")
    private LocalTime duration;

    @ManyToMany(mappedBy = "events",
            cascade = CascadeType.ALL)
    private List<SquadUser> participants;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "type_id")
    private EventTypeEntity eventType;
}
