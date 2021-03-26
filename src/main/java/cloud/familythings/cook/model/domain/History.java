package cloud.familythings.cook.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("histories")
public class History {

    @Id
    private String id;
    private String dishId;
    private String notes;
    private LocalDate scheduledOn;
    private LocalDate finishedAt;
    private List<Waste> wastes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setScheduledOn(LocalDate scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public LocalDate getScheduledOn() {
        return scheduledOn;
    }

    public LocalDate getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDate finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setWastes(List<Waste> wastes) {
        this.wastes = wastes;
    }

    public List<Waste> getWastes() {
        return wastes;
    }

    public static class Waste {
        private String ingredientName;
        private String ingredientUnit;
        private Integer quantity;

        public void setIngredientName(String ingredientName) {
            this.ingredientName = ingredientName;
        }

        public String getIngredientName() {
            return ingredientName;
        }

        public void setIngredientUnit(String ingredientUnit) {
            this.ingredientUnit = ingredientUnit;
        }

        public String getIngredientUnit() {
            return ingredientUnit;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
