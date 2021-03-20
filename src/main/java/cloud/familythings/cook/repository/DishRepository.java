package cloud.familythings.cook.repository;

import cloud.familythings.cook.model.domain.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DishRepository extends MongoRepository<Dish, String> {

}
