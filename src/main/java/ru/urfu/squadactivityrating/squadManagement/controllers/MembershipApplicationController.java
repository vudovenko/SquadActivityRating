package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.security.securityUsers.entities.SecurityUser;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;
import ru.urfu.squadactivityrating.squadManagement.services.MembershipApplicationService;

/**
 * Контроллер для обработки заявок на вступление в отряд
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/squads/{squadId}/membership-applications")
public class MembershipApplicationController {

    private final MembershipApplicationService membershipApplicationService;

    /**
     * Метод для получения страницы со списком заявок
     *
     * @param squadId идентификатор отряда заявок
     * @param model   модель
     * @return страницу со списком заявок
     */
    @GetMapping
    public String getMembershipApplicationListPage(@PathVariable String squadId, Model model) {
        model.addAttribute("fightersWishingToJoin",
                membershipApplicationService.getBySquadId(Long.valueOf(squadId))
                        .stream().map(MembershipApplication::getSquadUser).toList());
        model.addAttribute("squadId", squadId);

        return "squadManagement/membershipApplication/membership-applications";
    }

    /**
     * Метод для отправки заявки на вступление в отряд
     *
     * @param squadId     идентификатор отряда, в который подается заявка
     * @param currentUser текущий авторизованный пользователь
     * @return страница с карточкой отряда
     */
    @GetMapping("/submit-application")
    public String submitApplication(@PathVariable Long squadId,
                                    @AuthenticationPrincipal SecurityUser currentUser) {
        membershipApplicationService.submitApplication(squadId, currentUser.getId());
        return "redirect:/squads/" + squadId;
    }

    /**
     * Метод для одобрения заявки на вступление в отряд
     *
     * @param squadId идентификатор отряда, в котором принимается заявка
     * @param userId  идентификатор пользователя, подавшего заявку
     * @return страница со списком заявок
     */
    @GetMapping("/approve/{userId}")
    public String approveFighter(@PathVariable Long squadId, @PathVariable Long userId) {
        membershipApplicationService.approveFighter(squadId, userId);

        return "redirect:/squads/" + squadId + "/membership-applications";
    }

    /**
     * Метод для отклонения заявки на вступление в отряд
     *
     * @param squadId идентификатор отряда, в котором отклоняется заявка
     * @param userId  идентификатор пользователя, подавшего заявку
     * @return страница со списком заявок
     */
    @GetMapping("/refuse/{userId}")
    public String refuseFighter(@PathVariable Long squadId, @PathVariable Long userId) {
        membershipApplicationService.refuseFighter(squadId, userId);

        return "redirect:/squads/" + squadId + "/membership-applications";
    }
}
