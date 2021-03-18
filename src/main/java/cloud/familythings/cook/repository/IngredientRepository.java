package cloud.familythings.cook.repository;

import cloud.familythings.cook.model.domain.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
}
