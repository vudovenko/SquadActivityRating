package ru.urfu.squadactivityrating.eventManagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_type")
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ru.urfu.squadactivityrating.eventManagement.entities.enums.EventType eventType;

    @OneToMany(mappedBy = "eventType",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private List<Event> events;

    @Override
    public String toString() {
        return eventType.toString();
    }
}
