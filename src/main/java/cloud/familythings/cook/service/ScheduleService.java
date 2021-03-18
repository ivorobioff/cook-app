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
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<Schedule> getAll() {
        List<Schedule> schedules = scheduleRepository
                .findAll(Sort.by("when").ascending());

        if (schedules.size() > 0) {
            Map<String, Ingredient> ingredients = ingredientRepository.findAll().stream()
                    .collect(toMap(Ingredient::getId, i -> i));

            Map<String, Dish> dishes = dishRepository.findAll().stream()
                    .peek(dish -> dish.setIngredients(dish.getIngredientIds()
                            .stream().map(ingredients::get).collect(toList())))
                    .collect(toMap(Dish::getId, d -> d));

            schedules.forEach(schedule -> schedule.setDish(dishes.get(schedule.getDishId())));
        }

        return schedules;
    }

    public Schedule create(Schedule schedule) {

        scheduleRepository.save(schedule);

        return schedule;
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

    public void cancel(String id) {
        scheduleRepository.deleteById(id);
    }
}
