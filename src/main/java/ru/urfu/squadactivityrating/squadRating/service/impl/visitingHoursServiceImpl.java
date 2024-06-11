package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.repository.VisitingHoursRepository;
import ru.urfu.squadactivityrating.squadRating.service.VisitingHoursService;

@Service
@RequiredArgsConstructor
public class visitingHoursServiceImpl implements VisitingHoursService {

    private final VisitingHoursRepository visitingHoursRepository;

    @Override
    public void deleteVisitingHours(VisitingHours visitingHours) {
        visitingHoursRepository.delete(visitingHours);
    }
}
