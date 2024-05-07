package ru.urfu.squadactivityrating.squadManagement.services;

import ru.urfu.squadactivityrating.squadManagement.entities.Squad;

import java.util.List;

/**
 * Сервис для работы с отрядами
 */
public interface SquadService {

    /**
     * Метод получения всех отрядов
     *
     * @return список отрядов
     */
    List<Squad> getAllSquads();

    /**
     * Метод удаления отряда
     *
     * @param squadId идентификатор отряда
     */
    void deleteSquad(Long squadId);

    /**
     * Метод сохранения отряда
     *
     * @param squad отряд
     * @return сохраненный отряд
     */
    Squad saveSquad(Squad squad);

    /**
     * Метод получения отряда по идентификатору
     *
     * @param squadId идентификатор отряда
     * @return отряд
     */
    Squad getSquadById(Long squadId);

    /**
     * Метод обновления отряда
     *
     * @param squadId             идентификатор отряда
     * @param squad               отряд с обновленными данными
     * @param selectedFightersIds идентификаторы добавленных в отряд участников
     */
    void updateSquad(Long squadId,
                     Squad squad,
                     Long[] selectedFightersIds);
}
