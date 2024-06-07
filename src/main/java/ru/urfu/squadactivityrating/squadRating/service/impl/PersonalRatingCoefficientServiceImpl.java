package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.repository.PersonalRatingCoefficientRepository;
import ru.urfu.squadactivityrating.squadRating.service.PersonalRatingCoefficientService;

@Service
@RequiredArgsConstructor
public class PersonalRatingCoefficientServiceImpl implements PersonalRatingCoefficientService {

    private final PersonalRatingCoefficientRepository personalRatingCoefficientRepository;
}
