package cloud.familythings.cook.service;

import cloud.familythings.cook.model.domain.History;
import cloud.familythings.cook.repository.HistoryRepository;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.Scopeable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public List<History> getAllByDishId(String dishId, Scope scope) {
        return historyRepository.findAllByDishId(dishId, new Scopeable(scope));
    }
}
