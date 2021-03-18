package cloud.familythings.cook.controller;

import cloud.familythings.cook.model.domain.History;
import cloud.familythings.cook.service.HistoryService;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.validation.InvalidPayloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE,
        path = "/api/v1.0"
)
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping(path = "/history")
    @ResponseStatus(HttpStatus.OK)
    public List<History> index(@RequestParam String dishId, Scope scope, BindingResult binding) {

        if (binding.hasErrors()) {
            throw new InvalidPayloadException(binding);
        }

        return historyService.getAllByDishId(dishId, scope);
    }
}
