package cloud.familythings.cook.util;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;

public class QueryUtils {
    public static void dishContainsIngredients(Query query, Collection<String> ingredientIds) {
        query.addCriteria(Criteria.where("requiredIngredients")
                .elemMatch(Criteria.where("ingredientId").in(ingredientIds)));
    }

    public static Query dishContainsIngredients(Collection<String> ingredientIds) {
        Query query = new Query();
        dishContainsIngredients(query, ingredientIds);
        return query;
    }
}
