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

        public Waste() {

        }

        public Waste(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        private String name;
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
