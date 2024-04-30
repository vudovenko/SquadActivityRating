package ru.urfu.squadactivityrating.squadManagement.entities;

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
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "commander_id")
    private SquadUser commander;

    @OneToMany(mappedBy = "squad",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private List<SquadUser> users;

    @ManyToMany(mappedBy = "applicationsForMembershipInSquads",
            cascade = CascadeType.ALL)
    private List<SquadUser> membershipApplications;
}
