package ru.urfu.squadactivityrating.eventManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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

    @ElementCollection(targetClass = EventType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "event_type", joinColumns = @JoinColumn(name = "event_id"))
    @Enumerated(EnumType.STRING)
    private Set<EventType> roles;
}
