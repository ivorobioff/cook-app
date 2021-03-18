package cloud.familythings.cook.service;

import cloud.familythings.cook.model.domain.Ingredient;
import cloud.familythings.cook.model.filter.IngredientFilter;
import cloud.familythings.cook.repository.DishRepository;
import cloud.familythings.cook.repository.IngredientRepository;
import eu.techmoodivns.support.data.RegexUtils;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.Scopeable;
import eu.techmoodivns.support.validation.InvalidOperationException;
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.techmoodivns.support.random.RandomUtils.transportProperties;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Ingredient> getAll(IngredientFilter filter, Scope scope) {

        Query query = new Query();

        if (filter.getName() != null) {
            query.addCriteria(Criteria.where("name")
                    .regex(RegexUtils.contains(filter.getName())));
        }

        query.with(new Scopeable(scope));

        List<Ingredient> ingredients =  mongoTemplate.find(query, Ingredient.class);

        if (ingredients.size() > 0) {
            Set<String> usedIds = dishRepository.findAll().stream()
                    .flatMap(dish -> dish.getIngredientIds().stream())
                    .collect(Collectors.toSet());

            ingredients.forEach(ingredient -> ingredient.setUsedByDish(usedIds.contains(ingredient.getId())));
        }

        return ingredients;
    }

    public Ingredient create(Ingredient ingredient) {

        ingredientRepository.save(ingredient);

        return ingredient;
    }

    public void update(String id, Ingredient updates) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        transportProperties(updates, ingredient);

        ingredientRepository.save(ingredient);
    }

    public void remove(String id) {

        if (dishRepository.existsByIngredientIds(id)) {
            throw new InvalidOperationException("This ingredient cannot be delete because it's used by some of dishes!");
        }

        ingredientRepository.deleteById(id);
    }
}
