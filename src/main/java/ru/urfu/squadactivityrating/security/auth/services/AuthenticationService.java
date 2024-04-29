package ru.urfu.squadactivityrating.security.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.security.auth.dto.AuthenticationRequest;
import ru.urfu.squadactivityrating.security.auth.dto.AuthenticationResponse;
import ru.urfu.squadactivityrating.security.auth.dto.RegisterRequest;
import ru.urfu.squadactivityrating.security.configurations.JwtService;
import ru.urfu.squadactivityrating.security.securityUser.entities.SecurityUser;
import ru.urfu.squadactivityrating.security.securityUser.enums.UserRole;
import ru.urfu.squadactivityrating.security.securityUser.services.SecurityUserService;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SecurityUserService securityUserService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Регистрирует нового пользователя на основе данных из запроса.
     * Создает нового пользователя, сохраняет его в репозитории и генерирует JWT токен.
     *
     * @param request объект запроса с данными нового пользователя
     */
    public void register(RegisterRequest request) {
        // Создание нового пользователя на основе данных из запроса
        SecurityUser securityUser = SecurityUser.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(UserRole.USER))
                .build();

        securityUserService.saveUser(securityUser);
    }

    /**
     * Аутентификация пользователя на основе переданного запроса.
     * При успешной аутентификации генерируется JWT токен и возвращается объект ответа с токенами доступа и обновления.
     *
     * @param request объект запроса с данными для аутентификации
     * @return объект ответа со сгенерированными JWT токенами доступа и обновления
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        /*Аутентификация пользователя на основе email и пароля из запроса.
         * authenticationManager выполняет всю работу для аутентификации пользователя.
         * В случае, если имя пользователя или пароль неверны, будет выброшено исключение.*/
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Поиск пользователя по email в репозитории
        SecurityUser securityUser = securityUserService.getUserByEmail(request.getEmail());

        // Генерация JWT токена для пользователя
        String jwtToken = jwtService.generateToken(securityUser);

        // Возвращение объекта ответа со сгенерированным JWT токеном
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
