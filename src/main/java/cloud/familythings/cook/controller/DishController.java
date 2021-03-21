package cloud.familythings.cook.controller;

import cloud.familythings.cook.model.domain.Dish;
import cloud.familythings.cook.model.filter.DishFilter;
import cloud.familythings.cook.service.DishService;
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
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping(path = "/dishes")
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> index(DishFilter filter, Scope scope, BindingResult binding) {

        if (binding.hasErrors()) {
            throw new InvalidPayloadException(binding);
        }

        return dishService.getAll(filter, scope);
    }

    @GetMapping(path = "/lightweight-dishes")
    @ResponseStatus(HttpStatus.OK)
    public List<Dish> lightweightIndex() {
        return dishService.getAllLightweight();
    }

    @PostMapping(path = "/dishes")
    @ResponseStatus(HttpStatus.OK)
    public Dish create(@RequestBody Dish dish) {
        return dishService.create(dish);
    }

    @PatchMapping(path = "/dishes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dish update(@PathVariable String id, @RequestBody Dish dish) {
        return dishService.update(id, dish);
    }

    @DeleteMapping(path = "/dishes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable String id) {
        dishService.remove(id);
    }
}
