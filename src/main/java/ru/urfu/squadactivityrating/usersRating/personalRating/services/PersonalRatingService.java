package ru.urfu.squadactivityrating.usersRating.personalRating.services;

import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;

import java.util.List;

public interface PersonalRatingService {

    Pair<SquadUser, List<Pair<EventToSquadUser, Double>>> getUserToResults(SquadUser squadUser);

    Double getTotalScore(List<Pair<EventToSquadUser, Double>> eventsToScore);
}
