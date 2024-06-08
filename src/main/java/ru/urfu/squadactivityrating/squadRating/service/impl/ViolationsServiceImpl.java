package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.entitites.ViolationType;
import ru.urfu.squadactivityrating.squadRating.repository.ViolationsRepository;
import ru.urfu.squadactivityrating.squadRating.service.ViolationsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationsServiceImpl implements ViolationsService {

    private final ViolationsRepository violationsRepository;

    @Override
    public List<ViolationType> getAll() {
        return violationsRepository.findAll();
    }
}
