package eu.techmoodivns.opi.service;

import eu.techmoodivns.opi.model.domain.Subject;
import eu.techmoodivns.opi.repository.SubjectRepository;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.data.Scopeable;
import eu.techmoodivns.support.validation.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;
import static eu.techmoodivns.support.random.RandomUtils.transportProperties;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> getAll(Scope scope) {
        return getAll(scope, null);
    }

    public List<Subject> getAll(Scope scope, String term) {

        Scopeable scopeable = new Scopeable(scope);

        if (term != null) {
            Query query = TextQuery
                    .queryText(TextCriteria.forDefaultLanguage().matching(term))
                    .sortByScore()
                    .with(scopeable);

            return mongoTemplate.find(query, Subject.class);
        }

        return subjectRepository.findAll(scopeable)
                .getContent();
    }

    public Subject create(Subject subject) {
        subjectRepository.save(subject);

        return subject;
    }

    public void update(String id, Subject updates) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        transportProperties(updates, subject);

        subjectRepository.save(subject);
    }

    public void remove(String id) {
        subjectRepository.deleteById(id);
    }
}
