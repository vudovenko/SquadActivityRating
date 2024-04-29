package ru.urfu.squadactivityrating.security.auth.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.squadactivityrating.security.auth.dto.AuthenticationRequest;
import ru.urfu.squadactivityrating.security.auth.dto.AuthenticationResponse;
import ru.urfu.squadactivityrating.security.auth.dto.RegisterRequest;
import ru.urfu.squadactivityrating.security.auth.services.AuthenticationService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/signup")
    public String registration(Model model) {
        model.addAttribute("registrationForm", new RegisterRequest());
        return "security/signup_page";
    }

    @PostMapping("/register")
    public String registration(RegisterRequest request, Model model) {
        service.register(request);
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "security/login_page";
    }

    @PostMapping("/authenticate")
    public String authenticate(
            AuthenticationRequest request,
            HttpServletResponse httpServletResponse) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);
        Cookie cookie = new Cookie("token", authenticationResponse.getToken());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return "redirect:/";
    }
}
