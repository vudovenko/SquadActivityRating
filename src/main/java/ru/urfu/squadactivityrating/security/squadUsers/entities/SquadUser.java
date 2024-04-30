package ru.urfu.squadactivityrating.security.squadUsers.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "squad_users")
public class SquadUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "security_id")
    private SecurityUser securityUser;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @OneToOne(mappedBy = "commander",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.REFRESH, CascadeType.MERGE})
    private Squad subordinateSquad;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "membership_applications",
            joinColumns = @JoinColumn(name = "squad_user_id"),
            inverseJoinColumns = @JoinColumn(name = "squad_id")
    )
    private List<Squad> applicationsForMembershipInSquads;
}
