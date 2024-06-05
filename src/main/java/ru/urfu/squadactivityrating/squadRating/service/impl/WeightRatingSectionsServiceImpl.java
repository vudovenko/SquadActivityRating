package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.squadRating.repository.WeightRatingSectionsServiceRepository;
import ru.urfu.squadactivityrating.squadRating.service.WeightRatingSectionsService;

@Service
@RequiredArgsConstructor
public class WeightRatingSectionsServiceImpl implements WeightRatingSectionsService {

    private final WeightRatingSectionsServiceRepository weightRatingSectionsServiceRepository;
}
