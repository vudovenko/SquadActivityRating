package ru.urfu.squadactivityrating.squadRating.entitites.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Violations {

    VERBAL_WARNING("Устное предупреждение"),
    REPRIMAND("Выговор"),
    SEVERE_REPRIMAND("Строгий выговор");

    private final String result;

    @Override
    public String toString() {
        return result;
    }
}
