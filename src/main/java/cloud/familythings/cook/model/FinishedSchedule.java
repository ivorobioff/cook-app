package cloud.familythings.cook.model;

import eu.techmoodivns.support.validation.validator.Distinctive;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import cloud.familythings.cook.model.domain.Dish.Ingredient;

public class FinishedSchedule {
    @NotBlank
    private String notes;

    @NotEmpty
    @Distinctive(byField = "name", caseInsensitive = true)
    private List<@Valid Ingredient> ingredients;

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
