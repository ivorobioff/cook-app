package cloud.familythings.cook.repository;

import cloud.familythings.cook.model.domain.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    void deleteByDishId(String dishId);
}
