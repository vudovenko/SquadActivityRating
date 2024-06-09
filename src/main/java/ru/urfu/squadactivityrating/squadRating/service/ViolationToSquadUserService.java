package ru.urfu.squadactivityrating.squadRating.service;

import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;

import java.util.List;

/**
 * Сервис для работы с сущностями связей между нарушениями и бойцами
 */
public interface ViolationToSquadUserService {

    /**
     * Метод для получения списка связей между нарушениями и бойцами
     *
     * @return список связей между нарушениями и бойцами
     */
    List<ViolationToSquadUser> getAll();

    /**
     * Метод для получения списка связей между нарушениями и бойцами с параметром фильтрации по решённости нарушения
     *
     * @param isSolved искать решенные или нерешенные нарушения
     * @return список связей между нарушениями и бойцами
     */
    List<ViolationToSquadUser> getAllByIsSolved(boolean isSolved);

    List<ViolationToSquadUser> getAllUnsolvedViolationsBySquad(Squad squad);
}
