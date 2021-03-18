package eu.techmoodivns.opi.controller;

import eu.techmoodivns.opi.model.domain.Subject;
import eu.techmoodivns.opi.service.SubjectService;
import eu.techmoodivns.support.data.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE,
        path = "/api/v1.0"
)
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping(path = "/subjects")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> index(Scope scope, @RequestParam(required = false) String term) {
        return subjectService.getAll(scope, term);
    }

    @PostMapping(path = "/subjects")
    @ResponseStatus(HttpStatus.OK)
    public Subject create(@RequestBody Subject subject) {
        return subjectService.create(subject);
    }

    @PatchMapping(path = "/subjects/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody Subject subject) {
        subjectService.update(id, subject);
    }

    @DeleteMapping(path = "/subjects/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable String id) {
        subjectService.remove(id);
    }
}
