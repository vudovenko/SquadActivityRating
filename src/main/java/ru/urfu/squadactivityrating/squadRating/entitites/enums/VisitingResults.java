package ru.urfu.squadactivityrating.squadRating.entitites.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VisitingResults {

    PLACE1("1 место"),
    PLACE2("2 место"),
    PLACE3("3 место"),
    PARTICIPATION("Участие");

    private final String result;

    @Override
    public String toString() {
        return result;
    }
}
