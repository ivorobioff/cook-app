package cloud.familythings.cook.model.domain;

import cloud.familythings.cook.repository.IngredientRepository;
import eu.techmoodivns.support.validation.validator.Distinctive;
import eu.techmoodivns.support.validation.validator.Exists;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Document("dishes")
public class Dish {

    @Id
    private String id;

    @NotBlank
    private String name;

    private String notes;

    @NotEmpty
    @Distinctive(byField = "ingredientId")
    private List<@Valid RequiredIngredient> requiredIngredients;

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

    public void setRequiredIngredients(List<RequiredIngredient> requiredIngredients) {
        this.requiredIngredients = requiredIngredients;
    }

    public List<RequiredIngredient> getRequiredIngredients() {
        return requiredIngredients;
    }

    public void setLastFinishedAt(LocalDate lastFinishedAt) {
        this.lastFinishedAt = lastFinishedAt;
    }

    public LocalDate getLastFinishedAt() {
        return lastFinishedAt;
    }

    public static class RequiredIngredient {

        @NotBlank
        @Exists(repository = IngredientRepository.class)
        private String ingredientId;

        @Transient
        private Ingredient ingredient;

        @NotNull
        @Positive
        private Integer quantity;

        public void setIngredientId(String ingredientId) {
            this.ingredientId = ingredientId;
        }

        public String getIngredientId() {
            return ingredientId;
        }

        public void setIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        public Ingredient getIngredient() {
            return ingredient;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
