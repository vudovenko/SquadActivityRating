package ru.urfu.squadactivityrating.usersRating.personalRating.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.personalRating.entities.PersonalRatingCoefficient;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;
import ru.urfu.squadactivityrating.squadRating.service.PersonalRatingCoefficientService;
import ru.urfu.squadactivityrating.squadRating.service.ViolationToSquadUserService;
import ru.urfu.squadactivityrating.squadRating.service.impl.VisitingResultServiceImpl;
import ru.urfu.squadactivityrating.usersRating.personalRating.services.PersonalRatingService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalRatingServiceImpl implements PersonalRatingService {

    private final EventToSquadUserService eventToSquadUserService;
    private final ViolationToSquadUserService violationToSquadUserService;
    private final PersonalRatingCoefficientService personalRatingCoefficientService;

    @Override
    public Pair<SquadUser, List<Pair<EventToSquadUser, Double>>> getUserToResults(SquadUser squadUser) {
        List<EventToSquadUser> eventsToSquadUser
                = eventToSquadUserService.getBySquadUserId(squadUser.getId());
        Pair<SquadUser, List<Pair<EventToSquadUser, Double>>> userToResults
                = getUserToResults(squadUser, eventsToSquadUser);

        return userToResults;
    }

    @Override
    public Double getTotalScore(List<Pair<EventToSquadUser, Double>> eventsToScore, SquadUser squadUser) {
        Double sum = 0.0;

        for (Pair<EventToSquadUser, Double> eventToScore : eventsToScore) {
            sum += eventToScore.getSecondValue();
        }
        List<ViolationToSquadUser> userViolations
                = violationToSquadUserService.getAllUnsolvedViolationsBySquadUser(squadUser);
        Double amountPenalties = VisitingResultServiceImpl.round(
                VisitingResultServiceImpl.getAmountPenalties(userViolations), 1);

        return sum - amountPenalties;
    }

    private Pair<SquadUser, List<Pair<EventToSquadUser, Double>>> getUserToResults(
            SquadUser squadUser, List<EventToSquadUser> eventsToSquadUser
    ) {
        List<Pair<EventToSquadUser, Double>> eventsToScores = new ArrayList<>();
        Pair<SquadUser, List<Pair<EventToSquadUser, Double>>> userToResults
                = new Pair<>(squadUser, eventsToScores);

        eventsToSquadUser.forEach(
                eventToSquadUser -> {
                    Pair<EventToSquadUser, Double> eventToScore
                            = new Pair<>(eventToSquadUser, getScoreForEvent(eventToSquadUser));
                    eventsToScores.add(eventToScore);
                }
        );

        return userToResults;
    }

    private Double getScoreForEvent(EventToSquadUser eventToSquadUser) {
        Double score;

        Event event = eventToSquadUser.getEvent();
        EventTypes eventTypes = event.getEventType().getEventTypeValue();
        if (eventToSquadUser.getVisitingResult() == null
                && eventToSquadUser.getVisitingHours() == null) {
            return 0.0;
        }
        if (eventTypes == EventTypes.SOCIAL_WORK
                || eventTypes == EventTypes.PRODUCTION_WORK) {
            score = getScoreForDuration(getDuration(
                            eventToSquadUser.getVisitingHours().getStartTime(),
                            eventToSquadUser.getVisitingHours().getEndTime()),
                    eventTypes);
        } else {
            score = VisitingResultServiceImpl.getWeightByType(
                    eventTypes,
                    eventToSquadUser.getVisitingResult());
        }

        return VisitingResultServiceImpl.round(score, 1);
    }

    private Double getScoreForDuration(Duration duration, EventTypes eventTypes) {
        Double durationInHours = (double) duration.toHours() + ((double) duration.toMinutesPart() / 60);
        PersonalRatingCoefficient personalRatingCoefficient
                = personalRatingCoefficientService.getByEventType(eventTypes);

        return durationInHours * personalRatingCoefficient.getPersonalRatingCoefficient();
    }

    private Duration getDuration(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        long secondsBetween = ChronoUnit.SECONDS.between(dateTime1, dateTime2);

        return Duration.ofSeconds(secondsBetween);
    }
}
