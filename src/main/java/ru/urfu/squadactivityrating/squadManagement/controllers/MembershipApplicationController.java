package ru.urfu.squadactivityrating.squadManagement.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.squadManagement.services.MembershipApplicationService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/squads/{id}/membership-applications")
public class MembershipApplicationController {

    private final MembershipApplicationService membershipApplicationService;

    @GetMapping
    public String getMembershipApplicationListPage(Model model, @PathVariable String id) {
        model.addAttribute("membershipApplications",
                membershipApplicationService.getBySquadId(Long.valueOf(id)));
        return "squadManagement/membership-applications";
    }
}
