package cloud.familythings.cook.controller;

import cloud.familythings.cook.model.domain.Ingredient;
import cloud.familythings.cook.model.filter.IngredientFilter;
import cloud.familythings.cook.service.IngredientService;
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
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @GetMapping(path = "/ingredients")
    @ResponseStatus(HttpStatus.OK)
    public List<Ingredient> index(IngredientFilter filter, Scope scope, BindingResult binding) {

        if (binding.hasErrors()) {
            throw new InvalidPayloadException(binding);
        }

        return ingredientService.getAll(filter, scope);
    }

    @GetMapping(path = "/lightweight-ingredients")
    @ResponseStatus(HttpStatus.OK)
    public List<Ingredient> lightweightIndex() {
        return ingredientService.getAllLightweight();
    }

    @PostMapping(path = "/ingredients")
    @ResponseStatus(HttpStatus.OK)
    public Ingredient create(@RequestBody Ingredient ingredient) {
        return ingredientService.create(ingredient);
    }

    @PatchMapping(path = "/ingredients/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody Ingredient ingredient) {
        ingredientService.update(id, ingredient);
    }

    @DeleteMapping(path = "/ingredients/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable String id) {
        ingredientService.remove(id);
    }
}
