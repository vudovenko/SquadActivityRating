package ru.urfu.squadactivityrating.squadManagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.squadManagement.entities.MembershipApplication;
import ru.urfu.squadactivityrating.squadManagement.services.MembershipApplicationService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squads/{squadId}/membership-applications")
public class MembershipApplicationController {

    private final MembershipApplicationService membershipApplicationService;

    @GetMapping
    public String getMembershipApplicationListPage(@PathVariable String squadId, Model model) {
        model.addAttribute("fightersWishingToJoin",
                membershipApplicationService.getBySquadId(Long.valueOf(squadId))
                        .stream().map(MembershipApplication::getSquadUser).toList());
        model.addAttribute("squadId", squadId);

        return "squadManagement/membershipApplication/membership-applications";
    }

    @GetMapping("/approve/{userId}")
    public String approveFighter(@PathVariable Long squadId, @PathVariable Long userId) {
        membershipApplicationService.approveFighter(squadId, userId);

        return "redirect:/squads/" + squadId + "/membership-applications";
    }

    @GetMapping("/refuse/{userId}")
    public String refuseFighter(@PathVariable Long squadId, @PathVariable Long userId) {
        membershipApplicationService.refuseFighter(squadId, userId);

        return "redirect:/squads/" + squadId + "/membership-applications";
    }
}
