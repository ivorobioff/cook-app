package eu.techmoodivns.support.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.techmoodivns.support.random.RandomUtils.resolveValue;

public class DistinctValidator implements ConstraintValidator<Distinct, List<?>> {

    private String field;

    @Override
    public void initialize(Distinct annotation) {
        field = annotation.value();
    }

    @Override
    public boolean isValid(List<?> values, ConstraintValidatorContext context) {

        Set<String> uniqueValues = values.stream()
                .map(v -> (String) resolveValue(v, field))
                .collect(Collectors.toSet());

        return uniqueValues.size() == values.size();
    }
}
