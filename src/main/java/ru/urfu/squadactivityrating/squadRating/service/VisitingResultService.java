package ru.urfu.squadactivityrating.squadRating.service;

import org.springframework.ui.Model;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;

public interface VisitingResultService {

    void setVisitingResultsInModel(EventTypes eventTypes, Model model);
}
