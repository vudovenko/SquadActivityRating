package ru.urfu.squadactivityrating.squadRating.entitites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class Pair<T1, T2> {

    private T1 firstValue;
    private T2 secondValue;
}
