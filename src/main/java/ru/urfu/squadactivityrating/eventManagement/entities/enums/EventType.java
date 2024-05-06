package ru.urfu.squadactivityrating.eventManagement.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

    SPORT("Спорт"),
    CREATIVE_WORK("Творческая работа"),
    SOCIAL_WORK("Социальная работа"),
    PRODUCTION_WORK("Производственная работа"),
    PARTICIPATION_IN_EVENTS("Участие в мероприятиях"),
    PARTICIPATION_IN_EVENTS_URFU("Участие в мероприятиях Штаба СО УрФУ"),
    AGITSEKTOR("Агитсектор"),
    DISCIPLINE("Дисциплина");

    private final String typeName;

    @Override
    public String toString() {
        return typeName;
    }
}
