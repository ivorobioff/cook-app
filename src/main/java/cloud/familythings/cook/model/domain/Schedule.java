package cloud.familythings.cook.model.domain;

import cloud.familythings.cook.repository.DishRepository;
import eu.techmoodivns.support.validation.validator.Exists;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document("schedules")
public class Schedule {

    @Id
    private String id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime scheduledOn;

    @NotBlank
    @Exists(repository = DishRepository.class)
    private String dishId;

    @Transient
    private Dish dish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(LocalDateTime scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
