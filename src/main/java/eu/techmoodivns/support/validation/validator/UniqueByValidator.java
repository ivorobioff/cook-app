package eu.techmoodivns.support.validation.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static eu.techmoodivns.support.random.RandomUtils.resolveValue;
import static java.util.Collections.emptyList;

public class UniqueByValidator implements ConstraintValidator<UniqueBy, Object> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    private UniqueBy annotation;

    private Qualifier qualifier;

    @Override
    public void initialize(UniqueBy annotation) {
        this.annotation = annotation;

        Class<? extends Qualifier> qualifierType = annotation.qualifier();

        if (qualifierType != NullQualifier.class) {
            qualifier = beanFactory.createBean(qualifierType);
        }
    }

    @Override
    public boolean isValid(Object source, ConstraintValidatorContext context) {

        if (source == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(annotation.message())
                .addPropertyNode(annotation.value())
                .addConstraintViolation();

        String target = (String) resolveValue(source, annotation.value());

        if (target == null) {
            return true;
        }

        String id = (String) resolveValue(source, annotation.id());

        return !exists(source, target, id);
    }

    private boolean exists(Object source, String value, String id) {

        Query q = new Query()
                .addCriteria(Criteria.where(annotation.value()).is(value.trim()));

        if (qualifier != null) {
            qualifier.getCriteria().forEach(q::addCriteria);
        }

        if (id != null) {
            q.addCriteria(Criteria.where("id").ne(id));
        }

        return mongoTemplate.exists(q, source.getClass());
    }

    public interface Qualifier {
        List<CriteriaDefinition> getCriteria();
    }

    public static class NullQualifier implements Qualifier {

        @Override
        public List<CriteriaDefinition> getCriteria() {
            return emptyList();
        }
    }
}
