package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;
import ru.urfu.squadactivityrating.squadRating.service.WeightRatingSectionsService;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightInSection1Or2;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightInSection5Or6;
import ru.urfu.squadactivityrating.weightSettings.repositories.VisitingResultsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class VisitingResultServiceImpl implements VisitingResultService {

    private final EventToSquadUserService eventToSquadUserService;
    private final WeightRatingSectionsService weightRatingSectionsService;
    private final VisitingResultsRepository visitingResultsRepository;

    @Override
    public void setVisitingResultsInModel(EventTypes eventTypes, Model model) {
        // todo код надо рефакторить
        List<EventToSquadUser> eventsToSquadUsersByEventType = eventToSquadUserService
                .getEventsToSquadUsersByEventType(eventTypes);
        List<Double> finalScores = new ArrayList<>();

        if (eventTypes == EventTypes.SPORT
                || eventTypes == EventTypes.CREATIVE_WORK
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU) {
            List<Pair<Double, Integer>> totalPlaces = new ArrayList<>();
            setResultVisitToModelForTypes1And2(
                    eventsToSquadUsersByEventType,
                    eventTypes,
                    totalPlaces,
                    model);
            Comparator<Pair<Double, Integer>> comparator
                    = Comparator.comparingDouble(Pair::getFirstValue);
            addFinalPlaces(totalPlaces, comparator);
            model.addAttribute("totalPlaces", totalPlaces);
            finalScores = getFinalScores(totalPlaces, comparator,
                    (pair, maxValue) -> pair.getFirstValue() / maxValue, eventTypes);
        } else if (eventTypes == EventTypes.SOCIAL_WORK
                || eventTypes == EventTypes.PRODUCTION_WORK) {
            List<Pair<Duration, Integer>> totalPlaces = new ArrayList<>();
            setResultVisitToModelForTypes3And4(
                    eventsToSquadUsersByEventType,
                    totalPlaces,
                    model);
            Comparator<Pair<Duration, Integer>> comparator
                    = Comparator.comparing(Pair::getFirstValue);
            addFinalPlaces(totalPlaces, comparator);
            model.addAttribute("totalPlaces", totalPlaces);
            finalScores = getFinalScores(totalPlaces, comparator,
                    (pair, maxValue) -> (double) pair.getFirstValue().toSeconds()
                            / maxValue.toSeconds(),
                    eventTypes);
        }
        model.addAttribute("finalScores", finalScores);
    }

    /**
     * Метод, который устанавливает результат посещения мероприятий
     * отрядами для спорта и творческой работы.
     *
     * @param eventsToSquadUsersByEventType список мероприятий, из которых достаются баллы для каждого отряда
     * @param eventTypes                    тип мероприятий
     * @param totalPlaces                   список, в котором хранятся итоговые суммы баллов за все мероприятия по указанному типу
     * @param model                         модель
     */
    private void setResultVisitToModelForTypes1And2(
            List<EventToSquadUser> eventsToSquadUsersByEventType,
            EventTypes eventTypes,
            List<Pair<Double, Integer>> totalPlaces,
            Model model) {
        /* Создаем структуру, в которой будут храниться результаты посещения мероприятий.
         * Каждому отряду будет соответствовать словарь с мероприятиями и результатами участия.
         * На данном этапе заполняем значениями по умолчанию (отсутствие мест - 0 баллов)*/
        LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>>
                squadVisitingResults
                = setVisitingResultsInModelByType(eventsToSquadUsersByEventType,
                () -> new Pair<>(new ArrayList<>(), 0.0), model);

        // todo если нет мероприятий, то выбрасывается ошибка при null
        // todo также добавить тестовые данные
        /* Проходимся по списку посещений.
           Заполняем структуру squadVisitingResults парой (выйгранные места - общая сумма баллов)*/
        for (EventToSquadUser eventToSquadUser : eventsToSquadUsersByEventType) {
            Squad squad = eventToSquadUser.getSquadUser().getSquad();
            Pair<List<VisitingResult>, Double> pair = squadVisitingResults
                    .get(squad)
                    .get(eventToSquadUser.getEvent());
            pair.getFirstValue().add(eventToSquadUser.getVisitingResult());
            pair.setSecondValue(round(pair.getSecondValue() +
                    getWeightByType(eventTypes, eventToSquadUser.getVisitingResult()), 2));
        }

        /* получаем итоговые суммы баллов за каждое мероприятия для каждого отряда,
           но без итоговых мест относительно других отрядов*/
        for (Map.Entry<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> entry
                : squadVisitingResults.entrySet()) {
            Double sum = round(entry.getValue().values()
                    .stream().mapToDouble(Pair::getSecondValue).sum(), 2);

            totalPlaces.add(new Pair<>(sum, 0));
        }
        model.addAttribute("squadVisitingResults", squadVisitingResults);
    }

    /**
     * Метод, который округляет вещественное число до указанного количества знаков после запятой
     *
     * @param number число для округления
     * @param scale  количество знаков после запятой
     * @return округленное число
     */
    private static double round(double number, int scale) {
        BigDecimal bigDecimal = BigDecimal.valueOf(number);
        bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * Возвращает вес результата посещения мероприятия в зависимости от типа мероприятия
     *
     * @param eventTypes     тип мероприятия
     * @param visitingResult результат посещения
     * @return балл за результат участия в мероприятии
     */
    private Double getWeightByType(EventTypes eventTypes, VisitingResult visitingResult) {

        return switch (eventTypes) {
            case SPORT -> visitingResult.getWeightInSection1();
            case CREATIVE_WORK -> visitingResult.getWeightInSection2();
            case PARTICIPATION_IN_EVENTS -> visitingResult.getWeightInSection5();
            case PARTICIPATION_IN_EVENTS_URFU -> visitingResult.getWeightInSection6();
            default -> throw new IllegalStateException("У типа мероприятия " + eventTypes + " нет веса");
        };
    }

    /**
     * Метод, который устанавливает результат посещения мероприятий
     * * отрядами для социальной и производственной работы.
     *
     * @param eventsToSquadUsersByEventType список мероприятий, из которых достаются баллы для каждого отряда
     * @param totalPlaces                   список, в котором хранятся итоговые суммы баллов за все мероприятия по указанному типу
     * @param model                         модель
     */
    private void setResultVisitToModelForTypes3And4(
            List<EventToSquadUser> eventsToSquadUsersByEventType,
            List<Pair<Duration, Integer>> totalPlaces,
            Model model) {
        /* Создаем структуру, в которой будут храниться результаты посещения мероприятий.
         * Каждому отряду будет соответствовать словарь с мероприятиями и результатами участия.
         * На данном этапе заполняем значениями по умолчанию (duration = 0)*/
        LinkedHashMap<Squad, LinkedHashMap<Event, Duration>>
                squadVisitingResults
                = setVisitingResultsInModelByType(eventsToSquadUsersByEventType,
                () -> Duration.ZERO, model);

        // todo если нет мероприятий, то выбрасывается ошибка при null
        // todo также добавить тестовые данные
        /* Проходимся по списку посещений.
           Заполняем структуру squadVisitingResults, увеличивая Duration.*/
        for (EventToSquadUser eventToSquadUser : eventsToSquadUsersByEventType) {
            Squad squad = eventToSquadUser.getSquadUser().getSquad();
            Duration totalDuration = squadVisitingResults
                    .get(squad)
                    .get(eventToSquadUser.getEvent());
            VisitingHours visitingHours = eventToSquadUser.getVisitingHours();
            if (visitingHours != null) {
                Duration durationUserParticipation = Duration
                        .between(visitingHours.getStartTime(), visitingHours.getEndTime());
                totalDuration = totalDuration.plus(durationUserParticipation);
                squadVisitingResults
                        .get(squad)
                        .put(eventToSquadUser.getEvent(), totalDuration);
            }
        }
        model.addAttribute("squadVisitingResults", squadVisitingResults);

        /* получаем сумму часов за каждое мероприятия для каждого отряда,
           но без итоговых мест относительно других отрядов*/
        for (Map.Entry<Squad, LinkedHashMap<Event, Duration>> entry
                : squadVisitingResults.entrySet()) {
            Duration durationSum = entry.getValue().values()
                    .stream().reduce(Duration.ZERO, Duration::plus);

            totalPlaces.add(new Pair<>(durationSum, 0));
        }
    }

    /**
     * Метод, который получает и возвращает заготовку структуры для отображения результатов посещений мероприятий.
     * <br>
     * Устанавливает значения по умолчанию для каждого посещения.
     * <br>
     * Например, отряд №1 - 1 мероприятие - 0 часов, Отряд №2 - 2 мероприятие - 0 часов и т.д.
     *
     * @param eventsToSquadUsersByEventType посещения, для которых устанавливаются значения по умолчанию
     * @param defaultValue                  значение по умолчанию
     * @param model                         модель
     * @param <T>                           тип для баллов или часов
     * @return структура, с результатами посещения мероприятий отрядами в виде значений по умолчанию
     */
    private <T> LinkedHashMap<Squad, LinkedHashMap<Event, T>>
    setVisitingResultsInModelByType(List<EventToSquadUser> eventsToSquadUsersByEventType,
                                    Supplier<T> defaultValue,
                                    Model model) {
        LinkedHashSet<Event> events =
                new LinkedHashSet<>(eventsToSquadUsersByEventType
                        .stream()
                        .map(EventToSquadUser::getEvent)
                        .sorted(Comparator.comparing(Event::getDate))
                        .toList());
        model.addAttribute("events", events);
        LinkedHashMap<Squad, LinkedHashMap<Event, T>> squadVisitingResults
                = new LinkedHashMap<>();

        for (EventToSquadUser eventToSquadUser : eventsToSquadUsersByEventType) {
            Squad squad = eventToSquadUser.getSquadUser().getSquad();
            if (!squadVisitingResults.containsKey(squad)) {
                squadVisitingResults.put(squad, new LinkedHashMap<>());
            }
            for (Event event : events) {
                if (!squadVisitingResults.get(squad).containsKey(event)) {
                    squadVisitingResults.get(squad).put(event, defaultValue.get());
                }
            }
        }
        return squadVisitingResults;
    }

    /**
     * Метод для назначения итоговых мест в исходном списке по сумме баллов, полученных за участие в мероприятиях
     *
     * @param totalPlaces список с итоговыми баллами и назначенными итоговыми местами
     * @param comparator  компаратор для сортировки пар с итоговыми местами
     * @param <T>         тип для баллов или часов
     */
    public static <T> void addFinalPlaces(List<Pair<T, Integer>> totalPlaces,
                                          Comparator<Pair<T, Integer>> comparator) {
        // Копия списка для сортировки
        List<Pair<T, Integer>> sortedPlaces = new ArrayList<>(totalPlaces);
        sortedPlaces.sort(comparator);
        Collections.reverse(sortedPlaces); // Сортировка в порядке убывания

        // Карта для хранения мест
        Map<T, Integer> placeMap = new HashMap<>();
        int place = 1;
        for (int i = 0; i < sortedPlaces.size(); i++) {
            if (i > 0 && !sortedPlaces.get(i).getFirstValue().equals(sortedPlaces.get(i - 1).getFirstValue())) {
                place = i + 1;
            }
            placeMap.put(sortedPlaces.get(i).getFirstValue(), place);
        }

        // Назначение мест в исходном списке
        for (Pair<T, Integer> pair : totalPlaces) {
            pair.setSecondValue(placeMap.get(pair.getFirstValue()));
        }
    }

    /**
     * Метод, который список итоговых баллов.
     *
     * @param totalPlaces          список пар с суммарными баллами/часами и итоговыми местами.
     * @param comparator           компаратор для поиска пары с самыми большими баллами/часами.
     * @param finalScoreCalculator функция для расчета итогового балла.
     * @param eventTypes           тип мероприятий
     * @param <T>                  тип для баллов или часов
     * @return список с итоговыми баллами
     */
    public  <T> List<Double> getFinalScores(List<Pair<T, Integer>> totalPlaces,
                                           Comparator<Pair<T, Integer>> comparator,
                                           BiFunction<Pair<T, Integer>, T, Double>
                                                   finalScoreCalculator,
                                           EventTypes eventTypes) {
        Optional<Pair<T, Integer>> maxPair = totalPlaces
                .stream()
                .max(comparator);
        if (maxPair.isPresent()) {
            List<Double> finalScores = new ArrayList<>();
            Integer weightRatingSection = weightRatingSectionsService
                    .findByEventTypes(eventTypes).getWeightRatingSection();
            for (Pair<T, Integer> pair : totalPlaces) {
                Double coefficient = finalScoreCalculator
                        .apply(pair, maxPair.get().getFirstValue());
                Double scores = round(coefficient * weightRatingSection, 1);
                finalScores.add(scores);
            }

            return finalScores;
        }
        throw new IllegalStateException("Максимальное значение не найдено");
    }

    public WeightInSection1Or2 getWeightsInSection1() {
        WeightInSection1Or2 weightInSection1 = WeightInSection1Or2.builder().build();
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                weightInSection1.setPlace1(visitingResult.getWeightInSection1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                weightInSection1.setPlace2(visitingResult.getWeightInSection1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                weightInSection1.setPlace3(visitingResult.getWeightInSection1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                weightInSection1.setParticipation(visitingResult.getWeightInSection1());
            }
        }
        return weightInSection1;
    }

    public WeightInSection1Or2 getWeightsInSection2() {
        WeightInSection1Or2 weightInSection2 = WeightInSection1Or2.builder().build();
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                weightInSection2.setPlace1(visitingResult.getWeightInSection2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                weightInSection2.setPlace2(visitingResult.getWeightInSection2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                weightInSection2.setPlace3(visitingResult.getWeightInSection2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                weightInSection2.setParticipation(visitingResult.getWeightInSection2());
            }
        }
        return weightInSection2;
    }

    public WeightInSection5Or6 getWeightsInSection5() {
        WeightInSection5Or6 weightInSection5 = WeightInSection5Or6.builder().build();
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                weightInSection5.setPlace1(visitingResult.getWeightInSection5());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                weightInSection5.setPlace2(visitingResult.getWeightInSection5());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                weightInSection5.setPlace3(visitingResult.getWeightInSection5());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                weightInSection5.setParticipation(visitingResult.getWeightInSection5());
            } else {
                weightInSection5.setPresence(visitingResult.getWeightInSection5());
            }
        }
        return weightInSection5;
    }

    public WeightInSection5Or6 getWeightsInSection6() {
        WeightInSection5Or6 weightInSection6 = WeightInSection5Or6.builder().build();
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                weightInSection6.setPlace1(visitingResult.getWeightInSection6());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                weightInSection6.setPlace2(visitingResult.getWeightInSection6());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                weightInSection6.setPlace3(visitingResult.getWeightInSection6());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                weightInSection6.setParticipation(visitingResult.getWeightInSection6());
            } else {
                weightInSection6.setPresence(visitingResult.getWeightInSection6());
            }
        }
        return weightInSection6;
    }

    public void updateWeightsInSection1(WeightInSection1Or2 weightInSection1) {
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                visitingResult.setWeightInSection1(weightInSection1.getPlace1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                visitingResult.setWeightInSection1(weightInSection1.getPlace2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                visitingResult.setWeightInSection1(weightInSection1.getPlace3());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                visitingResult.setWeightInSection1(weightInSection1.getParticipation());
            }
            visitingResultsRepository.save(visitingResult);
        }
    }

    public void updateWeightsInSection2(WeightInSection1Or2 weightInSection2) {
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                visitingResult.setWeightInSection2(weightInSection2.getPlace1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                visitingResult.setWeightInSection2(weightInSection2.getPlace2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                visitingResult.setWeightInSection2(weightInSection2.getPlace3());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                visitingResult.setWeightInSection2(weightInSection2.getParticipation());
            }
            visitingResultsRepository.save(visitingResult);
        }
    }

    public void updateWeightsInSection5(WeightInSection5Or6 weightInSection5) {
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                visitingResult.setWeightInSection5(weightInSection5.getPlace1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                visitingResult.setWeightInSection5(weightInSection5.getPlace2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                visitingResult.setWeightInSection5(weightInSection5.getPlace3());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                visitingResult.setWeightInSection5(weightInSection5.getParticipation());
            } else {
                visitingResult.setWeightInSection5(weightInSection5.getPresence());
            }
            visitingResultsRepository.save(visitingResult);
        }
    }

    public void updateWeightsInSection6(WeightInSection5Or6 weightInSection6) {
        List<VisitingResult> visitingResults = visitingResultsRepository.findAll();
        for (VisitingResult visitingResult : visitingResults) {
            if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE1)) {
                visitingResult.setWeightInSection6(weightInSection6.getPlace1());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE2)) {
                visitingResult.setWeightInSection6(weightInSection6.getPlace2());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PLACE3)) {
                visitingResult.setWeightInSection6(weightInSection6.getPlace3());
            } else if (visitingResult.getVisitingResult().equals(VisitingResults.PARTICIPATION)) {
                visitingResult.setWeightInSection6(weightInSection6.getParticipation());
            } else {
                visitingResult.setWeightInSection6(weightInSection6.getPresence());
            }
            visitingResultsRepository.save(visitingResult);
        }
    }
}
