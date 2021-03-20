package cloud.familythings.cook.model;

import cloud.familythings.cook.repository.IngredientRepository;
import eu.techmoodivns.support.validation.validator.Distinctive;
import eu.techmoodivns.support.validation.validator.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class FinishedSchedule {
    @NotBlank
    private String notes;

    @NotEmpty
    @Distinctive(byField = "ingredientId")
    private List<@Valid Waste> wastes;

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setWastes(List<Waste> wastes) {
        this.wastes = wastes;
    }

    public List<Waste> getWastes() {
        return wastes;
    }

    public static class Waste {

        @NotBlank
        @Exists(repository = IngredientRepository.class)
        private String ingredientId;

        @NotNull
        @Positive
        private Integer quantity;

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setIngredientId(String ingredientId) {
            this.ingredientId = ingredientId;
        }

        public String getIngredientId() {
            return ingredientId;
        }
    }
}
