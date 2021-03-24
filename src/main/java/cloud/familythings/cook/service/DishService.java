package cloud.familythings.cook.service;

import cloud.familythings.cook.model.domain.Dish;
import cloud.familythings.cook.model.domain.Ingredient;
import cloud.familythings.cook.model.filter.DishFilter;
import cloud.familythings.cook.repository.DishRepository;
import cloud.familythings.cook.repository.HistoryRepository;
import cloud.familythings.cook.repository.IngredientRepository;
import cloud.familythings.cook.repository.ScheduleRepository;
import cloud.familythings.cook.util.QueryUtils;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.RegexUtils;
import eu.techmoodivns.support.data.Scopeable;
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;

import static eu.techmoodivns.support.random.RandomUtils.transportProperties;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Dish> getAll(DishFilter filter, Scope scope) {

        Query query = new Query();

        if (filter.getName() != null) {
            query.addCriteria(Criteria.where("name")
                    .regex(RegexUtils.contains(filter.getName())));
        }

        if (filter.getIngredientIds() != null) {
            QueryUtils.dishContainsIngredients(query, filter.getIngredientIds());
        }

        query.with(new Scopeable(scope));

        List<Dish> dishes = mongoTemplate.find(query, Dish.class);

        resolveDishes(dishes);

        return dishes;
    }

    public List<Dish> getAllLightweight() {
        return dishRepository.findAll();
    }

    public Dish create(Dish dish) {

        dishRepository.save(dish);

        resolveDishes(List.of(dish));

        return dish;
    }

    private void resolveDishes(List<Dish> dishes) {

        if (dishes.isEmpty()) {
            return ;
        }

        Set<String> dishIds = new HashSet<>();
        Set<String> usedIngredientIds = new HashSet<>();

        dishes.forEach(dish -> {
            usedIngredientIds.addAll(dish.getRequiredIngredients()
                    .stream()
                    .map(Dish.RequiredIngredient::getIngredientId)
                    .collect(toSet()));

            dishIds.add(dish.getId());
        });

        Map<String, Ingredient> usedIngredients = new HashMap<>();

        ingredientRepository.findAllById(usedIngredientIds)
                .forEach(ingredient -> usedIngredients.put(ingredient.getId(), ingredient));

        Map<String, LocalDateTime> histories = new HashMap<>();

        historyRepository.findAllByDishIdIn(dishIds).forEach(history -> {
            LocalDateTime lastFinishedAt = histories.get(history.getId());

            if (lastFinishedAt == null || history.getFinishedAt().isAfter(lastFinishedAt)) {
                histories.put(history.getDishId(), history.getFinishedAt());
            }
        });

        dishes.forEach(dish -> {
            dish.getRequiredIngredients().forEach(requiredIngredient -> {
                requiredIngredient.setIngredient(
                        usedIngredients.get(requiredIngredient.getIngredientId()));
            });

            dish.setLastFinishedAt(histories.get(dish.getId()));
        });
    }

    public Dish update(String id, Dish updates) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        transportProperties(updates, dish);

        dishRepository.save(dish);

        resolveDishes(List.of(dish));

        return dish;
    }

    public void remove(String id) {
        scheduleRepository.deleteByDishId(id);
        historyRepository.deleteByDishId(id);
        dishRepository.deleteById(id);
    }
}
