package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.repository.ViolationsRepository;
import ru.urfu.squadactivityrating.squadRating.service.ViolationsService;

@Service
@RequiredArgsConstructor
public class ViolationsServiceImpl implements ViolationsService {

    private final ViolationsRepository violationsRepository;
}
