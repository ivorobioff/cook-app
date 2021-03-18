package cloud.familythings.cook.model.filter;

public class DishFilter {
    private String name;
    private String ingredientId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientId() {
        return ingredientId;
    }
}
