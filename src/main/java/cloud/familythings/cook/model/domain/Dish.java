package cloud.familythings.cook.model.domain;

import eu.techmoodivns.support.validation.validator.Distinctive;
import eu.techmoodivns.support.validation.validator.UniqueBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@UniqueBy(value = "name", caseInsensitive = true)
@Document("dishes")
public class Dish {

    @Id
    private String id;

    @NotBlank
    private String name;

    private String notes;

    @NotEmpty
    @Distinctive(byField = "name", caseInsensitive = true)
    private List<@Valid Ingredient> ingredients;

    private LocalDate lastFinishedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setLastFinishedAt(LocalDate lastFinishedAt) {
        this.lastFinishedAt = lastFinishedAt;
    }

    public LocalDate getLastFinishedAt() {
        return lastFinishedAt;
    }

    public static class Ingredient {

        @NotBlank
        private String name;

        @NotBlank
        private String quantity;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getQuantity() {
            return quantity;
        }
    }
}
