package ru.urfu.squadactivityrating.usersRating.generalRating.services;

import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;

import java.util.List;

public interface GeneralRatingService {

    List<Pair<Integer, Pair<SquadUser, Double>>> getUsersToResults(List<SquadUser> usersToScores);
}
