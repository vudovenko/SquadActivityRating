package ru.urfu.squadactivityrating.squadRating.entitites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Класс для хранения пары значений
 *
 * @param <T1> первый тип значения
 * @param <T2> второй тип значения
 */
@AllArgsConstructor
@Data
@ToString
public class Pair<T1, T2> {

    private T1 firstValue;
    private T2 secondValue;
}
