package cloud.familythings.cook.service;

import cloud.familythings.cook.model.FinishedSchedule;
import cloud.familythings.cook.model.domain.Dish;
import cloud.familythings.cook.model.domain.History;
import cloud.familythings.cook.model.domain.Schedule;
import cloud.familythings.cook.repository.DishRepository;
import cloud.familythings.cook.repository.HistoryRepository;
import cloud.familythings.cook.repository.ScheduleRepository;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.Scopeable;
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.*;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private DishRepository dishRepository;

    public List<Schedule> getAll(Scope scope) {
        List<Schedule> schedules = scheduleRepository
                .findAll(new Scopeable(scope)).getContent();

        resolveSchedule(schedules);

        return schedules;
    }

    public Schedule create(Schedule schedule) {

        scheduleRepository.save(schedule);

        resolveSchedule(List.of(schedule));

        return schedule;
    }

    private void resolveSchedule(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            return ;
        }

        Set<String> usedDishIds = schedules.stream().map(Schedule::getDishId).collect(toSet());

        Map<String, Dish> usedDishes = new HashMap<>();

        dishRepository.findAllById(usedDishIds)
                .forEach(dish -> usedDishes.put(dish.getId(), dish));

        schedules.forEach(schedule -> schedule.setDish(usedDishes.get(schedule.getDishId())));
    }

    public void finish(String id, FinishedSchedule finished) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        List<FinishedSchedule.Waste> wasted = finished.getWastes();

        LocalDate finishedAt = LocalDate.now();

        History history = new History();
        history.setDishId(schedule.getDishId());
        history.setNotes(finished.getNotes());
        history.setScheduledOn(schedule.getScheduledOn());
        history.setFinishedAt(finishedAt);

        history.setWastes(wasted.stream()
                .map(w -> new History.Waste(w.getName(), w.getQuantity()))
                .collect(toList()));

        historyRepository.save(history);

        Dish dish = dishRepository.findById(schedule.getDishId())
                .orElseThrow();

        dish.setLastFinishedAt(finishedAt);

        dishRepository.save(dish);

        scheduleRepository.delete(schedule);
    }

    public void remove(String id) {
        scheduleRepository.deleteById(id);
    }
}
