package ru.urfu.squadactivityrating.squadRating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.urfu.squadactivityrating.eventManagement.entities.Event;
import ru.urfu.squadactivityrating.eventManagement.entities.enums.EventTypes;
import ru.urfu.squadactivityrating.eventManagement.entities.links.EventToSquadUser;
import ru.urfu.squadactivityrating.eventManagement.services.EventService;
import ru.urfu.squadactivityrating.eventManagement.services.EventToSquadUserService;
import ru.urfu.squadactivityrating.squadManagement.entities.Squad;
import ru.urfu.squadactivityrating.squadManagement.services.SquadService;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingHours;
import ru.urfu.squadactivityrating.squadRating.entitites.VisitingResult;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.FinalResultDTO;
import ru.urfu.squadactivityrating.squadRating.entitites.dto.Pair;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;
import ru.urfu.squadactivityrating.squadRating.service.ViolationToSquadUserService;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;
import ru.urfu.squadactivityrating.squadRating.service.WeightRatingSectionsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitingResultServiceImpl implements VisitingResultService {

    private final EventToSquadUserService eventToSquadUserService;
    private final EventService eventService;
    private final WeightRatingSectionsService weightRatingSectionsService;
    private final ViolationToSquadUserService violationToSquadUserService;
    private final SquadService squadService;

    @Override
    public SectionResult
    getPointsForEventsWithVisitingResults(EventTypes eventTypes) {
        LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> result = new LinkedHashMap<>();
        result = getResultWithAllSquads(result, LinkedHashMap::new);
        result = getResultWithAllEvents(result, eventTypes);
        result = getResultWithAllVisitingResults(result, eventTypes);
        LinkedHashMap<Squad, FinalResultDTO> finalResult = new LinkedHashMap<>();
        finalResult = getResultWithAllSquads(finalResult,
                () -> new FinalResultDTO(0.0, 0.0, 0));
        finalResult = getFinalResultWithAllVisitingResults(finalResult, result, eventTypes);
        return new SectionResult(result, finalResult);
    }

    @Override
    public List<Event> getEvents(LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> points) {
        if (points.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> eventMaps = points.values();
        Set<Event> eventSet = eventMaps.stream().findFirst().get().keySet();
        List<Event> events = new ArrayList<>(eventSet);

        return events;
    }

    public record SectionResult(LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> points,
                                LinkedHashMap<Squad, FinalResultDTO> finalPoints) {
    }

    private <T> LinkedHashMap<Squad, T> getResultWithAllSquads(
            LinkedHashMap<Squad, T> result,
            Supplier<T> supplier
    ) {
        List<Squad> allSquads = getSortedSquads();

        allSquads.forEach(
                squad -> {
                    result.put(squad, supplier.get());
                }
        );

        return result;
    }

    private List<Squad> getSortedSquads() {
        List<Squad> allSquads = squadService.getAllSquads();
        allSquads.sort(Comparator.comparing(Squad::getName));
        return allSquads;
    }

    private LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> getResultWithAllEvents(
            LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> result,
            EventTypes eventTypes
    ) {
        List<Event> events = eventService.getEventsByType(eventTypes);
        events.sort(Comparator.comparing(Event::getDate));
        result.keySet().forEach(
                squad -> {
                    events.forEach(
                            event -> {
                                result.get(squad).put(event, new Pair<>(new ArrayList<>(), 0.0));
                            }
                    );
                }
        );

        return result;
    }

    private LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> getResultWithAllVisitingResults(
            LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> result,
            EventTypes eventTypes
    ) {
        Map<Event, List<EventToSquadUser>> eventToEventToSquadUsers
                = eventToSquadUserService.getEventsToSquadUsersByEventType(eventTypes)
                .stream()
                .filter(eventToSquadUser -> eventToSquadUser.getVisitingResult() != null)
                .collect(Collectors.groupingBy(EventToSquadUser::getEvent));
        result.forEach(
                (squad, events) -> {
                    events.forEach(
                            (event, pair) -> {
                                List<EventToSquadUser> eventToSquadUsers = eventToEventToSquadUsers
                                        .get(event);
                                if (eventToSquadUsers != null) {
                                    eventToSquadUsers = eventToSquadUsers
                                            .stream()
                                            .filter(eTSU1 -> eTSU1.getSquadUser().getSquad().equals(squad))
                                            .toList();
                                    eventToSquadUsers.forEach(
                                            eventToSquadUser -> {
                                                setVisitingResult(pair, eventToSquadUser, eventTypes);
                                            }
                                    );
                                    sortVisitingResults(eventTypes, pair);
                                }
                            }
                    );
                }
        );

        return result;
    }

    private void sortVisitingResults(EventTypes eventTypes,
                                     Pair<List<VisitingResult>, Double> pair) {
        pair.getFirstValue()
                .sort((vr1, vr2) -> {
                            Double weight1 = getWeightByType(eventTypes, vr1);
                            Double weight2 = getWeightByType(eventTypes, vr2);

                            return Double.compare(weight2, weight1);
                        }
                );
    }

    private LinkedHashMap<Squad, FinalResultDTO> getFinalResultWithAllVisitingResults(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> result,
            EventTypes eventTypes) {
        calculateTotalPoints(finalResult, result);
        calculateFinalPoints(finalResult, eventTypes);
        calculateFinalPlaces(finalResult, Comparator.comparing(pair -> pair.getSecondValue().getFinalPoints()));

        return finalResult;
    }

    private static void calculateTotalPoints(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> result) {
        result.forEach(
                (squad, eventsToResults) -> {
                    eventsToResults.forEach(
                            (event, pair) -> {
                                FinalResultDTO finalResultDTO = finalResult.get(squad);
                                Double pointForEvent = pair.getSecondValue();
                                finalResultDTO.addToTotalPoints(pointForEvent);
                            }
                    );
                }
        );
    }

    private LinkedHashMap<Squad, FinalResultDTO> calculateFinalPoints(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            EventTypes eventTypes) {
        Optional<FinalResultDTO> maxFinalResultOptional = finalResult
                .values()
                .stream()
                .max(Comparator.comparing(FinalResultDTO::getTotalPoints));

        if (maxFinalResultOptional.isPresent()) {
            FinalResultDTO maxFinalResultDto = maxFinalResultOptional.get();
            Integer weightRatingSection = weightRatingSectionsService
                    .findByEventTypes(eventTypes).getWeightRatingSection();
            finalResult.values().forEach(
                    (finalResultDTO) -> {
                        Double coefficient = maxFinalResultDto.getTotalPoints() != 0
                                ? finalResultDTO.getTotalPoints()
                                / maxFinalResultDto.getTotalPoints()
                                : 0.0;
                        Double finalPoints = round(coefficient * weightRatingSection, 1);
                        finalResultDTO.setFinalPoints(finalPoints);
                    }
            );
        }

        return finalResult;
    }

    private static void calculateFinalPlaces(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            Comparator<Pair<Squad, FinalResultDTO>> comparator) {
        List<Pair<Squad, FinalResultDTO>> finalResultsList
                = finalResult.entrySet()
                .stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .toList();
        finalResultsList = new ArrayList<>(finalResultsList);
        finalResultsList.sort(comparator);
        Collections.reverse(finalResultsList);

        // Карта для хранения мест
        int place = 1;
        for (int i = 0; i < finalResultsList.size(); i++) {
            Squad currentSquad = finalResultsList.get(i).getFirstValue();
            if (i > 0) {
                Squad previousSquad = finalResultsList.get(i - 1).getFirstValue();
                if (!currentSquad.equals(previousSquad)) {
                    place = i + 1;
                }
            }
            FinalResultDTO finalResultDTO = finalResult.get(currentSquad);
            finalResultDTO.setFinalPlace(place);
        }
    }

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

    private void setVisitingResult(Pair<List<VisitingResult>, Double> pair,
                                   EventToSquadUser eventToSquadUser,
                                   EventTypes eventTypes) {
        VisitingResult visitingResult = eventToSquadUser.getVisitingResult();
        pair.getFirstValue()
                .add(visitingResult);
        pair.setSecondValue(round(pair.getSecondValue() +
                getWeightByType(eventTypes, visitingResult), 2));
    }

    // todo это ужасный код. Надо переписать, но нет времени. Удачи тому, кто возьмется это переписывать.
    //  Думаю, как минимум не надо передавать модель в методы. Модель надо выпилить из аргументов методов.

    @Override
    public LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> getTotalVisitingResultsFromModel(Model model) {
        LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>>
                totalSquadVisitingResults
                = new LinkedHashMap<>();

        for (EventTypes eventType : EventTypes.values()) {
            if (!eventType.equals(EventTypes.DISCIPLINE)
                    && !eventType.equals(EventTypes.AGITSEKTOR)) {
                totalSquadVisitingResults
                        = processTotalSquadVisitingResults(totalSquadVisitingResults, eventType, model);
            }
        }

        return totalSquadVisitingResults;
    }

    @Override
    public List<Pair<Double, Integer>> getFinalPlacesFromTotalResult(
            LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> totalSquadVisitingResults) {
        List<Pair<Double, Integer>> finalPlaces = new ArrayList<>();
        totalSquadVisitingResults.values().forEach(
                (eventTypeToScore) -> {
                    Pair<Double, Integer> scoreToPlace = new Pair<>(0.0, 0);
                    finalPlaces.add(scoreToPlace);
                    Double totalScore = 0.0;
                    for (Double score : eventTypeToScore.values()) {
                        totalScore += score;
                    }
                    scoreToPlace.setFirstValue(round(totalScore, 1));
                }
        );
        supplementWithDiscipline(totalSquadVisitingResults);
        addDisciplinePenaltiesInFinalScores(totalSquadVisitingResults.keySet(), finalPlaces);
        addFinalPlaces(
                finalPlaces,
                Comparator.comparingDouble(Pair::getFirstValue));

        return finalPlaces;
    }

    private void supplementWithDiscipline(LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>>
                                                  totalSquadVisitingResults) {
        totalSquadVisitingResults.keySet().forEach(
                squad -> {
                    List<ViolationToSquadUser> squadViolations
                            = violationToSquadUserService.getAllUnsolvedViolationsBySquad(squad);
                    Double amountPenalties = round(getAmountPenalties(squadViolations), 1);

                    totalSquadVisitingResults.get(squad).put(EventTypes.DISCIPLINE, amountPenalties);
                }
        );
    }

    private void addDisciplinePenaltiesInFinalScores(Set<Squad> squads,
                                                     List<Pair<Double, Integer>> finalPlaces) {
        int counter = 0;
        for (Squad squad : squads) {
            List<ViolationToSquadUser> unsolvedViolations
                    = violationToSquadUserService.getAllUnsolvedViolationsBySquad(squad);
            Pair<Double, Integer> scoreToPlace = finalPlaces.get(counter);

            Double finalScore = scoreToPlace.getFirstValue() - getAmountPenalties(unsolvedViolations);
            scoreToPlace.setFirstValue(round(finalScore, 1));
            counter++;
        }
    }

    private Double getAmountPenalties(List<ViolationToSquadUser> violationsToSquadUser) {
        Double amountPenalties = 0.0;
        for (ViolationToSquadUser violationToSquadUser : violationsToSquadUser) {
            Double penalty = violationToSquadUser
                    .getViolation().getViolationType().getWeight();

            amountPenalties += penalty;
        }

        return amountPenalties;
    }

    public LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>>
    processTotalSquadVisitingResults(LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>>
                                             totalSquadVisitingResults,
                                     EventTypes eventType, Model model) {
        List<Double> finalScores;
        setVisitingResultsInModel(eventType, model);
        LinkedHashMap<Squad, Object> squadVisitingResults
                = (LinkedHashMap<Squad, Object>) model
                .getAttribute("squadVisitingResults");
        finalScores = (List<Double>)
                model.getAttribute("finalScores");
        int counter = 0;
        // todo если ни один отряд не посещал мероприятие, то таблица будет отображаться неправильно
        for (Squad squad : squadVisitingResults.keySet()) {
            if (!totalSquadVisitingResults.containsKey(squad)) {
                totalSquadVisitingResults
                        .put(squad, new LinkedHashMap<>());
            }
            LinkedHashMap<EventTypes, Double> eventTypeToScore
                    = totalSquadVisitingResults.get(squad);
            if (finalScores != null) {
                eventTypeToScore.put(eventType, finalScores.get(counter));
            }
            counter++;
        }
        if (squadVisitingResults.keySet().size() == 0) {
            LinkedHashMap<EventTypes, Double> eventMap = new LinkedHashMap<>();
            eventMap.put(eventType, 0.0);
            totalSquadVisitingResults.put(null, eventMap);
        }

        return totalSquadVisitingResults;
    }

    public void setVisitingResultsInModel(EventTypes eventTypes, Model model) {
        // todo код надо рефакторить
        List<Double> finalScores = new ArrayList<>();

        if (eventTypes == EventTypes.SPORT
                || eventTypes == EventTypes.CREATIVE_WORK
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
                || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU) {
            List<EventToSquadUser> eventsToSquadUsersByEventType = eventToSquadUserService
                    .getEventsToSquadUsersByEventType(eventTypes)
                    .stream()
                    .filter(eventToSquadUser -> eventToSquadUser.getVisitingResult() != null)
                    .toList();
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
            List<EventToSquadUser> eventsToSquadUsersByEventType = eventToSquadUserService
                    .getEventsToSquadUsersByEventType(eventTypes);
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

//    /**
//     * Метод для назначения итоговых мест в исходном списке по сумме баллов, полученных за участие в мероприятиях
//     *
//     * @param totalPlaces список с итоговыми баллами и назначенными итоговыми местами
//     * @param comparator  компаратор для сортировки пар с итоговыми местами
//     * @param <T>         тип для баллов или часов
//     */
//    public static <T> void addFinalPlaces(List<Pair<T, Integer>> totalPlaces,
//                                          Comparator<Pair<T, Integer>> comparator) {
//        // Копия списка для сортировки
//        List<Pair<T, Integer>> sortedPlaces = new ArrayList<>(totalPlaces);
//        sortedPlaces.sort(comparator);
//        Collections.reverse(sortedPlaces); // Сортировка в порядке убывания
//
//        // Карта для хранения мест
//        Map<T, Integer> placeMap = new HashMap<>();
//        int place = 1;
//        for (int i = 0; i < sortedPlaces.size(); i++) {
//            if (i > 0 && !sortedPlaces.get(i).getFirstValue().equals(sortedPlaces.get(i - 1).getFirstValue())) {
//                place = i + 1;
//            }
//            placeMap.put(sortedPlaces.get(i).getFirstValue(), place);
//        }
//
//        // Назначение мест в исходном списке
//        for (Pair<T, Integer> pair : totalPlaces) {
//            pair.setSecondValue(placeMap.get(pair.getFirstValue()));
//        }
//    }

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
    public <T> List<Double> getFinalScores(List<Pair<T, Integer>> totalPlaces,
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
        return new ArrayList<>(List.of());
    }
}
