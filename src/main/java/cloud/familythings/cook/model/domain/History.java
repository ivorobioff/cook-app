package cloud.familythings.cook.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("histories")
public class History {

    @Id
    private String id;
    private String dishId;
    private String notes;
    private LocalDateTime scheduledOn;
    private LocalDateTime finishedAt;

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

    public void setScheduledOn(LocalDateTime scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public LocalDateTime getScheduledOn() {
        return scheduledOn;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}
