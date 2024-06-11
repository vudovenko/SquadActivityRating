package ru.urfu.squadactivityrating.weightSettings.controllers.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.urfu.squadactivityrating.weightSettings.exceptions.SumOfWeightsGreaterThan100Exception;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@ControllerAdvice
public class WeightSettingsExceptionController {

    @ExceptionHandler(SumOfWeightsGreaterThan100Exception.class)
    public String handleSumOfWeightsGreaterThan100Exception(Exception e)
            throws UnsupportedEncodingException {
        return "redirect:/weight-settings?message=" +
                URLEncoder.encode(e.getMessage(), "UTF-8");
    }
}
