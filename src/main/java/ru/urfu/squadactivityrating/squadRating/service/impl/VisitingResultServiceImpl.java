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
import ru.urfu.squadactivityrating.squadRating.entitites.enums.VisitingResults;
import ru.urfu.squadactivityrating.squadRating.entitites.links.ViolationToSquadUser;
import ru.urfu.squadactivityrating.squadRating.repository.VisitingResultRepository;
import ru.urfu.squadactivityrating.squadRating.service.ViolationToSquadUserService;
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;
import ru.urfu.squadactivityrating.squadRating.service.WeightRatingSectionsService;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightInSection1Or2;
import ru.urfu.squadactivityrating.weightSettings.dto.WeightInSection5Or6;
import ru.urfu.squadactivityrating.weightSettings.repositories.VisitingResultsRepository;
import ru.urfu.squadactivityrating.utils.functionalInterfaces.TriFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitingResultServiceImpl implements VisitingResultService {

    private final EventToSquadUserService eventToSquadUserService;
    private final EventService eventService;
    private final WeightRatingSectionsService weightRatingSectionsService;
    private final ViolationToSquadUserService violationToSquadUserService;
    private final SquadService squadService;
    private final VisitingResultRepository visitingResultRepository;

    @Override
    public List<VisitingResult> getAll() {
        return visitingResultRepository.findAll();
    }
    private final VisitingResultsRepository visitingResultsRepository;

    @Override
    public VisitingResult findByType(VisitingResults visitingResults) {
        return visitingResultRepository.findByVisitingResult(visitingResults);
    }

    @Override
    public SectionResult<EventTypes, Double> getTotalPointsForAllEvents() {
        LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> totalResult = new LinkedHashMap<>();
        totalResult = getResultWithAllSquads(totalResult, LinkedHashMap::new);
        totalResult = getTotalResultWithAllEventTypes(totalResult);
        totalResult = fillInTotalScoreWithPoints(totalResult);
        LinkedHashMap<Squad, FinalResultDTO> finalPlacesForTotalResult = new LinkedHashMap<>();
        finalPlacesForTotalResult = getResultWithAllSquads(finalPlacesForTotalResult, FinalResultDTO::new);
        finalPlacesForTotalResult = getFinalPlacesForTotalResult(finalPlacesForTotalResult, totalResult);


        return new SectionResult<>(totalResult, finalPlacesForTotalResult);
    }

    LinkedHashMap<Squad, FinalResultDTO> getFinalPlacesForTotalResult(
            LinkedHashMap<Squad, FinalResultDTO> finalPlacesForTotalResult,
            LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> totalResult) {
        totalResult.forEach(
                (squad, eventTypesToPoints) -> {
                    eventTypesToPoints.forEach(
                            (eventTypes, points) -> {
                                FinalResultDTO finalResultDTO = finalPlacesForTotalResult.get(squad);
                                if (eventTypes.equals(EventTypes.DISCIPLINE)) {
                                    finalResultDTO.addToFinalPoints(-points);
                                } else {
                                    finalResultDTO.addToFinalPoints(points);
                                }
                            }
                    );
                }
        );
        calculateFinalPlaces(finalPlacesForTotalResult,
                Comparator.comparing(pair -> pair.getSecondValue().getFinalPoints()));

        return finalPlacesForTotalResult;
    }

    private LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> fillInTotalScoreWithPoints(
            LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> totalResult
    ) {
        totalResult.forEach(
                (squad, eventTypesToPoints) -> {
                    eventTypesToPoints.keySet().forEach(
                            eventTypes -> {
                                if (eventTypes == EventTypes.SPORT
                                        || eventTypes == EventTypes.CREATIVE_WORK
                                        || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS
                                        || eventTypes == EventTypes.PARTICIPATION_IN_EVENTS_URFU) {
                                    VisitingResultServiceImpl.SectionResult<Event, Pair<List<VisitingResult>, Double>> sectionResult
                                            = getPointsForEventsWithVisitingResults(eventTypes);
                                    AbstractMap<Squad, FinalResultDTO> finalPoints = sectionResult.finalPoints();
                                    eventTypesToPoints.put(eventTypes, finalPoints.get(squad).getFinalPoints());
                                } else if (eventTypes == EventTypes.SOCIAL_WORK
                                        || eventTypes == EventTypes.PRODUCTION_WORK) {
                                    VisitingResultServiceImpl.SectionResult<Event, Duration> sectionResult
                                            = getPointsForEventsWithVisitingHours(eventTypes);
                                    AbstractMap<Squad, FinalResultDTO> finalPoints = sectionResult.finalPoints();
                                    eventTypesToPoints.put(eventTypes, finalPoints.get(squad).getFinalPoints());
                                } else if (eventTypes == EventTypes.DISCIPLINE) {
                                    supplementWithDiscipline(totalResult);
                                }
                            }
                    );
                }
        );

        return totalResult;
    }

    private LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> getTotalResultWithAllEventTypes(
            LinkedHashMap<Squad, LinkedHashMap<EventTypes, Double>> result) {
        for (EventTypes eventType : EventTypes.values()) {
            if (!eventType.equals(EventTypes.AGITSEKTOR)) {
                result.keySet().forEach(squad -> result.get(squad).put(eventType, 0.0));
            }
        }

        return result;
    }

    @Override
    public SectionResult<Event, Pair<List<VisitingResult>, Double>>
    getPointsForEventsWithVisitingResults(EventTypes eventTypes) {
        LinkedHashMap<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> result = new LinkedHashMap<>();
        result = getResultWithAllSquads(result, LinkedHashMap::new);
        result = getResultWithAllEvents(result, () -> new Pair<>(new ArrayList<>(), 0.0), eventTypes);
        result = getResultWithAllVisitingResults(result,
                this::setVisitingResult,
                eTSU -> eTSU.getVisitingResult() != null
                        && eTSU.getSquadUser().getSquad() != null,
                eventTypes);
        LinkedHashMap<Squad, FinalResultDTO> finalResult = new LinkedHashMap<>();
        finalResult = getResultWithAllSquads(finalResult,
                () -> new FinalResultDTO(0.0, 0.0, 0));
        finalResult = getFinalResultWithAllVisitingResults(finalResult, result,
                (pair, finalResultDTO) -> {
                    Double pointForEvent = pair.getSecondValue();
                    finalResultDTO.addToTotalPoints(pointForEvent);
                },
                Comparator.comparing(FinalResultDTO::getTotalPoints),
                eventTypes);
        return new SectionResult<>(result, finalResult);
    }

    @Override
    public SectionResult<Event, Duration> getPointsForEventsWithVisitingHours(EventTypes eventTypes) {
        LinkedHashMap<Squad, LinkedHashMap<Event, Duration>> result = new LinkedHashMap<>();
        result = getResultWithAllSquads(result, LinkedHashMap::new);
        result = getResultWithAllEvents(result, () -> Duration.ZERO, eventTypes);
        result = getResultWithAllVisitingResults(result,
                this::increaseVisitingHours,
                eTSU -> eTSU.getVisitingHours() != null
                        && eTSU.getSquadUser().getSquad() != null,
                eventTypes);

        LinkedHashMap<Squad, FinalResultDTO> finalResult = new LinkedHashMap<>();
        finalResult = getResultWithAllSquads(finalResult,
                () -> new FinalResultDTO(Duration.ZERO, 0.0, 0));
        finalResult = getFinalResultWithAllVisitingResults(finalResult, result,
                (hoursForEvent, finalResultDTO) -> finalResultDTO.addToTotalHours(hoursForEvent),
                Comparator.comparing(FinalResultDTO::getTotalHours),
                eventTypes);
        return new SectionResult<>(result, finalResult);
    }

    @Override
    public <U, T> List<U> getEventsAndTypes(LinkedHashMap<Squad, LinkedHashMap<U, T>> points) {
        if (points.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<LinkedHashMap<U, T>> eventMaps = points.values();
        Set<U> eventSet = eventMaps.stream().findFirst().get().keySet();
        List<U> events = new ArrayList<>(eventSet);

        return events;
    }

    @Override
    public void deleteVisitingResult(VisitingResult visitingResult) {
        visitingResultRepository.delete(visitingResult);
    }

    public record SectionResult<T, U>(LinkedHashMap<Squad, LinkedHashMap<T, U>> points,
                                      LinkedHashMap<Squad, FinalResultDTO> finalPoints) {
    }

    private <T> LinkedHashMap<Squad, T> getResultWithAllSquads(
            LinkedHashMap<Squad, T> result,
            Supplier<T> supplier
    ) {
        List<Squad> allSquads = getSortedSquads();

        allSquads.forEach(
                squad -> result.put(squad, supplier.get())
        );

        return result;
    }

    private List<Squad> getSortedSquads() {
        List<Squad> allSquads = squadService.getAllSquads();
        allSquads.sort(Comparator.comparing(Squad::getName));
        return allSquads;
    }

    private <T> LinkedHashMap<Squad, LinkedHashMap<Event, T>> getResultWithAllEvents(
            LinkedHashMap<Squad, LinkedHashMap<Event, T>> result,
            Supplier<T> defaultValueSupplier,
            EventTypes eventTypes
    ) {
        List<Event> events = eventService.getEventsByType(eventTypes);
        LocalDateTime currentDate = LocalDateTime.now();
        events = new ArrayList<>(
                events
                        .stream()
                        .filter(event -> event.getDate().plus(event.getDuration()).isBefore(currentDate))
                        .toList());
        events
                .sort(Comparator.comparing(Event::getDate));
        for (Squad squad : result.keySet()) {
            for (Event event : events) {
                result.get(squad).put(event, defaultValueSupplier.get());
            }
        }

        return result;
    }

    private <T> LinkedHashMap<Squad, LinkedHashMap<Event, T>> getResultWithAllVisitingResults(
            LinkedHashMap<Squad, LinkedHashMap<Event, T>> result,
            TriFunction<T, EventToSquadUser, EventTypes, T> visitingResultSetter,
            Predicate<EventToSquadUser> filter,
            EventTypes eventTypes
    ) {
        Map<Event, List<EventToSquadUser>> eventToEventToSquadUsers
                = eventToSquadUserService.getEventsToSquadUsersByEventType(eventTypes)
                .stream()
                .filter(filter)
                .collect(Collectors.groupingBy(EventToSquadUser::getEvent));
        result.forEach(
                (squad, eventsToPairOrHours) -> {
                    eventsToPairOrHours.forEach(
                            (event, pairOrHours) -> {
                                List<EventToSquadUser> eventToSquadUsers = eventToEventToSquadUsers
                                        .get(event);
                                if (eventToSquadUsers != null) {
                                    eventToSquadUsers = eventToSquadUsers
                                            .stream()
                                            .filter(eTSU1 -> eTSU1.getSquadUser().getSquad().equals(squad))
                                            .toList();
                                    eventToSquadUsers.forEach(
                                            eventToSquadUser -> {

                                                if (eventTypes.equals(EventTypes.SOCIAL_WORK)
                                                        || eventTypes.equals(EventTypes.PRODUCTION_WORK)) {
                                                    AbstractMap<Event, T> eventTLinkedHashMap = result.get(squad);
                                                    T hours = eventTLinkedHashMap.get(event);
                                                    T visitingResHours = visitingResultSetter.apply(hours, eventToSquadUser, eventTypes);
                                                    eventTLinkedHashMap.put(event, visitingResHours);
                                                } else {
                                                    visitingResultSetter.apply(pairOrHours, eventToSquadUser, eventTypes);
                                                    sortVisitingResults(eventTypes,
                                                            (Pair<List<VisitingResult>, Double>) pairOrHours);
                                                }
                                            }
                                    );
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

    private <T> LinkedHashMap<Squad, FinalResultDTO> getFinalResultWithAllVisitingResults(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            LinkedHashMap<Squad, LinkedHashMap<Event, T>> result,
            BiConsumer<T, FinalResultDTO> recipientResultInDto,
            Comparator<FinalResultDTO> finalResultComparator,
            EventTypes eventTypes) {
        calculateTotalPoints(finalResult, result, recipientResultInDto);
        calculateFinalPoints(finalResult, finalResultComparator, eventTypes);
        calculateFinalPlaces(finalResult,
                Comparator.comparing(pair -> pair.getSecondValue().getFinalPoints()));

        return finalResult;
    }

    private static <T> void calculateTotalPoints(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            LinkedHashMap<Squad, LinkedHashMap<Event, T>> result,
            BiConsumer<T, FinalResultDTO> recipientResultInDto) {
        result.forEach(
                (squad, eventsToResults) -> {
                    eventsToResults.forEach(
                            (event, pairOrHours) -> {
                                FinalResultDTO finalResultDTO = finalResult.get(squad);
                                recipientResultInDto.accept(pairOrHours, finalResultDTO);
                            }
                    );
                }
        );
    }

    private LinkedHashMap<Squad, FinalResultDTO> calculateFinalPoints(
            LinkedHashMap<Squad, FinalResultDTO> finalResult,
            Comparator<FinalResultDTO> finalResultComparator,
            EventTypes eventTypes) {
        Optional<FinalResultDTO> maxFinalResultOptional = finalResult
                .values()
                .stream()
                .max(finalResultComparator);

        if (maxFinalResultOptional.isPresent()) {
            FinalResultDTO maxFinalResultDto = maxFinalResultOptional.get();
            Integer weightRatingSection = weightRatingSectionsService
                    .findByEventTypes(eventTypes).getWeightRatingSection();
            finalResult.values().forEach(
                    (finalResultDTO) -> {
                        Double coefficient;
                        if (eventTypes.equals(EventTypes.SOCIAL_WORK)
                                || eventTypes.equals(EventTypes.PRODUCTION_WORK)) {
                            coefficient = getCoefficient(
                                    (double) finalResultDTO.getTotalHours().toSeconds(),
                                    (double) maxFinalResultDto.getTotalHours().toSeconds());
                        } else {
                            coefficient = getCoefficient(
                                    finalResultDTO.getTotalPoints(),
                                    maxFinalResultDto.getTotalPoints());
                        }
                        Double finalPoints = round(coefficient * weightRatingSection, 1);
                        finalResultDTO.setFinalPoints(finalPoints);
                    }
            );
        }

        return finalResult;
    }

    private Double getCoefficient(Double currentValue, Double maxValue) {
        Double coefficient = maxValue != 0
                ? currentValue
                / maxValue
                : 0.0;

        return coefficient;
    }

    public static <T> void calculateFinalPlaces(
            LinkedHashMap<T, FinalResultDTO> finalResult,
            Comparator<Pair<T, FinalResultDTO>> comparator) {
        List<Pair<T, FinalResultDTO>> finalResultsList
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
            FinalResultDTO currentFinalResultDto = finalResultsList.get(i).getSecondValue();
            if (i > 0) {
                FinalResultDTO previousFinalResultDto = finalResultsList.get(i - 1).getSecondValue();
                if (!currentFinalResultDto.getFinalPoints()
                        .equals(previousFinalResultDto.getFinalPoints())) {
                    place += 1;
                }
            }
            FinalResultDTO finalResultDTO = finalResult.get(finalResultsList.get(i).getFirstValue());
            finalResultDTO.setFinalPlace(place);
        }
    }

    private Pair<List<VisitingResult>, Double>
    setVisitingResult(Pair<List<VisitingResult>, Double> pair,
                      EventToSquadUser eventToSquadUser,
                      EventTypes eventTypes) {
        VisitingResult visitingResult = eventToSquadUser.getVisitingResult();
        pair.getFirstValue()
                .add(visitingResult);
        pair.setSecondValue(round(pair.getSecondValue() +
                getWeightByType(eventTypes, visitingResult), 2));

        return pair;
    }

    private Duration increaseVisitingHours(Duration totalDuration,
                                           EventToSquadUser eventToSquadUser,
                                           EventTypes eventTypes) {
        VisitingHours visitingHours = eventToSquadUser.getVisitingHours();
        if (visitingHours != null) {
            Duration durationUserParticipation = Duration
                    .between(visitingHours.getStartTime(), visitingHours.getEndTime());
            totalDuration = totalDuration.plus(durationUserParticipation);
        }

        return totalDuration;
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

    public static Double getAmountPenalties(List<ViolationToSquadUser> violationsToSquadUser) {
        Double amountPenalties = 0.0;
        for (ViolationToSquadUser violationToSquadUser : violationsToSquadUser) {
            Double penalty = violationToSquadUser
                    .getViolation().getViolationType().getWeight();

            amountPenalties += penalty;
        }

        return amountPenalties;
    }

    /**
     * Метод, который округляет вещественное число до указанного количества знаков после запятой
     *
     * @param number число для округления
     * @param scale  количество знаков после запятой
     * @return округленное число
     */
    public static double round(double number, int scale) {
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
    public static Double getWeightByType(EventTypes eventTypes, VisitingResult visitingResult) {

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
