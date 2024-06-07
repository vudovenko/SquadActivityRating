package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.repository.ViolationToSquadUserRepository;
import ru.urfu.squadactivityrating.squadRating.service.ViolationToSquadUserService;

@Service
@RequiredArgsConstructor
public class ViolationToSquadUserServiceImpl implements ViolationToSquadUserService {

    private final ViolationToSquadUserRepository violationToSquadUserRepository;
}
