package eu.techmoodivns.opi.repository;

import eu.techmoodivns.opi.model.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubjectRepository extends MongoRepository<Subject, String> {

}
