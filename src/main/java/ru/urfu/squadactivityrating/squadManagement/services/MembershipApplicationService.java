package ru.urfu.squadactivityrating.squadManagement.services;

import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;

import java.util.List;

/**
 * Сервис, обрабатывающий заявки {@link MembershipApplication}
 */
public interface MembershipApplicationService {

    /**
     * Метод получения заявок по id отряда
     *
     * @param id идентификатор отряда
     * @return список заявок на вступление в отряд
     */
    List<MembershipApplication> getBySquadId(Long id);

    /**
     * Метод получения всех заявок на вступление в отряды
     *
     * @return список всех заявок
     */
    List<MembershipApplication> getAllMembershipApplications();

    /**
     * Метод подачи заявки на вступление в отряд
     *
     * @param squadId идентификатор отряда
     * @param userId  идентификатор пользователя
     */
    void submitApplication(Long squadId, Long userId);

    /**
     * Метод, подтверждающий вступление в отряд
     *
     * @param squadId идентификатор отряда, в который пользователь вступает
     * @param userId  идентификатор пользователя, вступающего в отряд
     */
    void approveFighter(Long squadId, Long userId);

    /**
     * Метод, отклоняющий вступление в отряд
     *
     * @param squadId идентификатор отряда, в который заявка была отклонена
     * @param userId  идентификатор пользователя, подавшего заявку
     */
    void refuseFighter(Long squadId, Long userId);
}
