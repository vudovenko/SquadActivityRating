package ru.urfu.squadactivityrating.weightSettings.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SumOfWeightsGreaterThan100Exception extends Exception {
    private final String message;
}
