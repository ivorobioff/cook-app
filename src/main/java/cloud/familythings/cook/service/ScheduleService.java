package cloud.familythings.cook.service;

import cloud.familythings.cook.model.FinishedSchedule;
import cloud.familythings.cook.model.domain.Dish;
import cloud.familythings.cook.model.domain.History;
import cloud.familythings.cook.model.domain.Ingredient;
import cloud.familythings.cook.model.domain.Schedule;
import cloud.familythings.cook.repository.DishRepository;
import cloud.familythings.cook.repository.HistoryRepository;
import cloud.familythings.cook.repository.IngredientRepository;
import cloud.familythings.cook.repository.ScheduleRepository;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.Scopeable;
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

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

        Map<String, Ingredient> ingredients = ingredientRepository.findAll().stream()
                .collect(toMap(Ingredient::getId, i -> i));

        List<Dish> dishes;

        if (schedules.size() > 1) {
            dishes = dishRepository.findAll();
        } else {
            dishes = List.of(dishRepository.findById(schedules.get(0).getDishId()).orElseThrow());
        }

        Map<String, Dish> normalizedDishes = dishes.stream()
                .peek(dish -> dish.setIngredients(dish.getIngredientIds()
                        .stream().map(ingredients::get).collect(toList())))
                .collect(toMap(Dish::getId, d -> d));

        schedules.forEach(schedule -> schedule.setDish(normalizedDishes.get(schedule.getDishId())));
    }

    public void finish(String id, FinishedSchedule finished) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        History history = new History();
        history.setDishId(schedule.getDishId());
        history.setNotes(finished.getNotes());
        history.setScheduledOn(schedule.getScheduledOn());
        history.setFinishedAt(LocalDateTime.now());

        historyRepository.save(history);

        scheduleRepository.delete(schedule);
    }

    public void remove(String id) {
        scheduleRepository.deleteById(id);
    }
}
