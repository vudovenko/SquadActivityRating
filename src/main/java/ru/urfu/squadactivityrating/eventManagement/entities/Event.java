package ru.urfu.squadactivityrating.eventManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.eventManagement.converters.DurationAttributeConverter;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

/**
 * Сущность события
 */
@Getter
@Setter
@ToString(exclude = {"participants"})
@EqualsAndHashCode(exclude = {"eventType", "participants"})
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
    private Boolean isItOnlyParticipation;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Convert(converter = DurationAttributeConverter.class)
    private Duration duration;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "type_id")
    private EventType eventType;

    @ManyToMany(mappedBy = "events",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private List<SquadUser> participants;

    public String getFormattedDate() {
        return getFormattedDate(date);
    }

    public static String getFormattedDate(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTimeFormatter.format(date);
    }

    public String getFormattedDuration() {
        StringJoiner joiner = new StringJoiner(" ");
        if (duration.toHoursPart() > 0) {
            joiner.add(String.format("%d час.", duration.toHours()));
        }
        if (duration.toMinutesPart() > 0) {
            joiner.add(String.format("%d мин.", duration.toMinutesPart()));
        }
        if (duration.toSecondsPart() > 0) {
            joiner.add(String.format("%d сек.", duration.toSecondsPart()));
        }

        return joiner.toString();
    }
}
