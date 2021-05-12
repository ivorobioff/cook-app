package cloud.familythings.cook.service;

import cloud.familythings.cook.model.domain.Dish;
import cloud.familythings.cook.model.filter.DishFilter;
import cloud.familythings.cook.repository.DishRepository;
import cloud.familythings.cook.repository.HistoryRepository;
import cloud.familythings.cook.repository.ScheduleRepository;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.RegexUtils;
import eu.techmoodivns.support.data.Scopeable;
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

import static eu.techmoodivns.support.random.RandomUtils.transportProperties;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

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

        if (filter.getNotes() != null) {
            query.addCriteria(Criteria.where("notes")
                    .regex(RegexUtils.contains(filter.getNotes())));
        }

        query.with(new Scopeable(scope));

        return mongoTemplate.find(query, Dish.class);
    }

    public List<Dish> getAllLightweight() {
        return dishRepository.findAll();
    }

    public Dish create(Dish dish) {

        dishRepository.save(dish);

        return dish;
    }

    public Dish update(String id, Dish updates) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        transportProperties(updates, dish);

        dishRepository.save(dish);

        return dish;
    }

    public void remove(String id) {
        scheduleRepository.deleteByDishId(id);
        historyRepository.deleteByDishId(id);
        dishRepository.deleteById(id);
    }
}
