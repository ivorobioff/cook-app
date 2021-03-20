package cloud.familythings.cook.model.filter;

import java.util.List;

public class DishFilter {
    private String name;
    private List<String> ingredientIds;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIngredientIds(List<String> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }

    public List<String> getIngredientIds() {
        return ingredientIds;
    }
}
