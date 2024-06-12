package ru.urfu.squadactivityrating.usersRating.generalRating.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.entities.SquadUser;
import ru.urfu.squadactivityrating.squadManagement.squadUsers.services.SquadUserService;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.usersRating.generalRating.services.GeneralRatingService;
import ru.urfu.squadactivityrating.usersRating.personalRating.services.PersonalRatingService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GeneralRatingServiceImpl implements GeneralRatingService {

    private final SquadUserService squadUserService;
    private final PersonalRatingService personalRatingService;

    @Override
    public List<Pair<Integer, Pair<SquadUser, Double>>> getUsersToResults() {
        Map<SquadUser, Double> usersToTotalScores
                = getAllUsersToTotalScores(squadUserService.getAllSquadUsers());
        List<Pair<Integer, Pair<SquadUser, Double>>> usersToTotalScoresWithPlaces
                = getUsersToTotalScoresWithPlaces(usersToTotalScores);

        return usersToTotalScoresWithPlaces;
    }

    private Map<SquadUser, Double> getAllUsersToTotalScores(List<SquadUser> allUsers) {
        Map<SquadUser, Double> usersToTotalScores = new LinkedHashMap<>();
        for (SquadUser squadUser : allUsers) {
            List<Pair<EventToSquadUser, Double>> visitingToScore
                    = personalRatingService
                    .getUserToResults(squadUser).getSecondValue();
            Double totalScore
                    = personalRatingService.getTotalScore(visitingToScore, squadUser);

            usersToTotalScores.put(squadUser, totalScore);
        }

        return usersToTotalScores;
    }

    private List<Pair<Integer, Pair<SquadUser, Double>>> getUsersToTotalScoresWithPlaces(Map<SquadUser, Double> usersToTotalScores) {
        // Создание списка пар (балл, пользователь) для сортировки
        List<Pair<Double, SquadUser>> scoresWithUsers = new ArrayList<>();
        for (Map.Entry<SquadUser, Double> entry : usersToTotalScores.entrySet()) {
            scoresWithUsers.add(new Pair<>(entry.getValue(), entry.getKey()));
        }

        // Сортировка списка по баллам в порядке убывания
        scoresWithUsers.sort(Comparator.comparing(Pair::getFirstValue));
        Collections.reverse(scoresWithUsers);

        // Инициализация списка для хранения результатов
        List<Pair<Integer, Pair<SquadUser, Double>>> result = new ArrayList<>();
        int place = 1;

        // Итерация по отсортированному списку и назначение мест
        for (int i = 0; i < scoresWithUsers.size(); i++) {
            Double currentScore = scoresWithUsers.get(i).getFirstValue();
            if (i > 0) {
                Double previousScore = scoresWithUsers.get(i - 1).getFirstValue();
                if (!currentScore.equals(previousScore)) {
                    place += 1;
                }
            }
            // Добавление результата в список
            result.add(new Pair<>(place, new Pair<>(scoresWithUsers.get(i).getSecondValue(),
                    scoresWithUsers.get(i).getFirstValue())));
        }

        return result;
    }
}
