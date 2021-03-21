package cloud.familythings.cook.repository;

import cloud.familythings.cook.model.domain.History;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface HistoryRepository extends MongoRepository<History, String> {
    void deleteByDishId(String dishId);
    List<History> findAllByDishId(String dishId, Pageable pageable);
    List<History> findAllByDishIdIn(Set<String> dishIds);
}
