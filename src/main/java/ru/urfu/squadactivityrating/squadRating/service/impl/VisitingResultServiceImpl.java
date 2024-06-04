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
import ru.urfu.squadactivityrating.squadRating.service.VisitingResultService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class VisitingResultServiceImpl implements VisitingResultService {

    private final EventToSquadUserService eventToSquadUserService;

    @Override
    public void setVisitingResultsInModel(EventTypes eventTypes, Model model) {
        List<EventToSquadUser> eventsToSquadUsersByEventType = eventToSquadUserService
                .getEventsToSquadUsersByEventType(eventTypes);
        List<Pair<Double, Integer>> totalPlaces = new ArrayList<>();

        if (eventTypes == EventTypes.SPORT || eventTypes == EventTypes.CREATIVE_WORK) {
            setResultVisitToModelForTypes1And2(
                    eventsToSquadUsersByEventType,
                    eventTypes,
                    totalPlaces,
                    model);
        } else if (eventTypes == EventTypes.SOCIAL_WORK || eventTypes == EventTypes.PRODUCTION_WORK) {
            setResultVisitToModelForTypes3And4(
                    eventsToSquadUsersByEventType,
                    eventTypes,
                    totalPlaces,
                    model);
        }

        addFinalPlaces(totalPlaces);
        model.addAttribute("totalPlaces", totalPlaces);
    }

    private void setResultVisitToModelForTypes3And4(
            List<EventToSquadUser> eventsToSquadUsersByEventType,
            EventTypes eventTypes,
            List<Pair<Double, Integer>> totalPlaces,
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
            Duration durationUserParticipation = Duration
                    .between(visitingHours.getStartTime(), visitingHours.getEndTime());
            totalDuration = totalDuration.plus(durationUserParticipation);
            squadVisitingResults
                    .get(squad)
                    .put(eventToSquadUser.getEvent(), totalDuration);
        }
        model.addAttribute("squadVisitingResults", squadVisitingResults);
    }

    /**
     * Метод, который устанавливает результат посещения мероприятий
     * отрядами для спорта и творческой работы.
     *
     * @param eventsToSquadUsersByEventType список мероприятий, из которых достаются баллы для каждого отряда
     * @param eventTypes                    тип мероприятия
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
                    getWeightByType(eventTypes, eventToSquadUser.getVisitingResult())));
        }

        /* получаем итоговые суммы баллов за каждое мероприятия для каждого отряда,
           но без итоговых мест относительно других отрядов*/
        for (Map.Entry<Squad, LinkedHashMap<Event, Pair<List<VisitingResult>, Double>>> entry
                : squadVisitingResults.entrySet()) {
            Double sum = round(entry.getValue().values()
                    .stream().mapToDouble(Pair::getSecondValue).sum());

            totalPlaces.add(new Pair<>(sum, 0));
        }
        model.addAttribute("squadVisitingResults", squadVisitingResults);
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
     * @param <T>                           тип значения по умолчанию для результата посещения
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
     * Устанавливает вес посещения мероприятия в зависимости от типа мероприятия
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
     * Метод для назначения итоговых мест в исходном списке по сумме баллов, полученных за участие в мероприятиях
     *
     * @param totalPlaces список с итоговыми баллами и назначенными итоговыми местами
     */
    public static void addFinalPlaces(List<Pair<Double, Integer>> totalPlaces) {
        // Копия списка для сортировки
        List<Pair<Double, Integer>> sortedPlaces = new ArrayList<>(totalPlaces);
        sortedPlaces.sort(Comparator.comparingDouble(Pair::getFirstValue));
        Collections.reverse(sortedPlaces); // Сортировка в порядке убывания

        // Карта для хранения мест
        Map<Double, Integer> placeMap = new HashMap<>();
        int place = 1;
        for (int i = 0; i < sortedPlaces.size(); i++) {
            if (i > 0 && !sortedPlaces.get(i).getFirstValue().equals(sortedPlaces.get(i - 1).getFirstValue())) {
                place = i + 1;
            }
            placeMap.put(sortedPlaces.get(i).getFirstValue(), place);
        }

        // Назначение мест в исходном списке
        for (Pair<Double, Integer> pair : totalPlaces) {
            pair.setSecondValue(placeMap.get(pair.getFirstValue()));
        }
    }

    private static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP); // Используйте другой режим округления, если нужно
        return bd.doubleValue();
    }
}
