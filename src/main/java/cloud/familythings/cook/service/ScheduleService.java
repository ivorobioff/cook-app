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

        Set<String> usedDishIds = schedules.stream().map(Schedule::getDishId).collect(toSet());

        Map<String, Dish> usedDishes = new HashMap<>();

        dishRepository.findAllById(usedDishIds)
                .forEach(dish -> usedDishes.put(dish.getId(), dish));

        Set<String> usedIngredientIds = usedDishes.values().stream()
                .flatMap(dish -> dish.getRequiredIngredients().stream())
                .map(Dish.RequiredIngredient::getIngredientId)
                .collect(toSet());

        Map<String, Ingredient> usedIngredients = new HashMap<>();

        ingredientRepository.findAllById(usedIngredientIds)
                .forEach(ingredient -> usedIngredients.put(ingredient.getId(), ingredient));

        usedDishes.forEach((dishId, dish) -> {
            dish.getRequiredIngredients().forEach(requiredIngredient ->
                    requiredIngredient.setIngredient(
                            usedIngredients.get(requiredIngredient.getIngredientId())));
        });

        schedules.forEach(schedule -> schedule.setDish(usedDishes.get(schedule.getDishId())));
    }

    public void finish(String id, FinishedSchedule finished) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        List<FinishedSchedule.Waste> wasted = finished.getWastes();

        Set<String> wastedIngredientIds = wasted.stream()
                .map(FinishedSchedule.Waste::getIngredientId)
                .collect(toSet());

        Map<String, Ingredient> wastedIngredients = new HashMap<>();

        ingredientRepository.findAllById(wastedIngredientIds)
                .forEach(ingredient -> wastedIngredients.put(ingredient.getId(), ingredient));


        LocalDate finishedAt = LocalDate.now();

        History history = new History();
        history.setDishId(schedule.getDishId());
        history.setNotes(finished.getNotes());
        history.setScheduledOn(schedule.getScheduledOn());
        history.setFinishedAt(finishedAt);

        history.setWastes(wasted.stream()
                .map(waste -> {
                    History.Waste historyWaste = new History.Waste();
                    historyWaste.setIngredientName(
                            wastedIngredients.get(waste.getIngredientId()).getName());
                    historyWaste.setIngredientUnit(
                            wastedIngredients.get(waste.getIngredientId()).getUnit());
                    historyWaste.setQuantity(waste.getQuantity());

                    return historyWaste;
                }).collect(toList()));

        historyRepository.save(history);

        List<Ingredient> changedIngredients = new ArrayList<>();

        wasted.forEach(waste -> {
            Ingredient ingredient = wastedIngredients.get(waste.getIngredientId());
            Integer oldQuantity = ingredient.getQuantity();
            Integer wastedQuantity = waste.getQuantity();
            int newQuantity = oldQuantity - wastedQuantity;

            if (newQuantity < 0) {
                newQuantity = 0;
            }

            ingredient.setQuantity(newQuantity);

            changedIngredients.add(ingredient);
        });

        ingredientRepository.saveAll(changedIngredients);

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
