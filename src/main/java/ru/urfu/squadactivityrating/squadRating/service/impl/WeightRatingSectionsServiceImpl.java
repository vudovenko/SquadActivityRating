package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.squadRating.entitites.WeightRatingSections;
import ru.urfu.squadactivityrating.squadRating.repository.WeightRatingSectionsServiceRepository;
import ru.urfu.squadactivityrating.squadRating.service.WeightRatingSectionsService;

@Service
@RequiredArgsConstructor
public class WeightRatingSectionsServiceImpl implements WeightRatingSectionsService {

    private final WeightRatingSectionsServiceRepository weightRatingSectionsServiceRepository;

    public WeightRatingSections findByEventTypes(EventTypes eventTypes) {
        if (weightRatingSectionsServiceRepository.findByEventTypes(eventTypes).isPresent()) {
            return weightRatingSectionsServiceRepository.findByEventTypes(eventTypes).get();
        }
        throw new IllegalArgumentException("Вес с типом " + eventTypes + " не найден!");
    }
}
