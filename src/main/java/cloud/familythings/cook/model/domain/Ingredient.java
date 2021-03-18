package cloud.familythings.cook.model.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Document("ingredients")
public class Ingredient {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotBlank
    private String unit;

    @Transient
    private Boolean usedByDish;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUsedByDish(boolean usedByDish) {
        this.usedByDish = usedByDish;
    }

    public boolean isUsedByDish() {
        return usedByDish;
    }
}
