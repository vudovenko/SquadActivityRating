package ru.urfu.squadactivityrating.squadManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.urfu.squadactivityrating.security.squadUsers.entities.SquadUser;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "squads")
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "squadId",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private List<SquadUser> users;
}
