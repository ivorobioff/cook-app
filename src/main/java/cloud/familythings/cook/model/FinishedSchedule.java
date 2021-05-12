package cloud.familythings.cook.model;

import eu.techmoodivns.support.validation.validator.Distinctive;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class FinishedSchedule {
    @NotBlank
    private String notes;

    @NotEmpty
    @Distinctive(byField = "ingredient", caseInsensitive = true)
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
        private String ingredient;

        @NotBlank
        private String quantity;

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getQuantity() {
            return quantity;
        }
    }
}
