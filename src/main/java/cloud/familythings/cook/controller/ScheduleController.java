package cloud.familythings.cook.controller;

import cloud.familythings.cook.model.FinishedSchedule;
import cloud.familythings.cook.model.domain.Schedule;
import cloud.familythings.cook.service.ScheduleService;
import eu.techmoodivns.support.data.Scope;
import eu.techmoodivns.support.validation.InvalidPayloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE,
        path = "/api/v1.0"
)
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping(path = "/schedules")
    @ResponseStatus(HttpStatus.OK)
    public List<Schedule> index(Scope scope) {
        return scheduleService.getAll(scope);
    }

    @PostMapping(path = "/schedules")
    @ResponseStatus(HttpStatus.OK)
    public Schedule create(@RequestBody Schedule schedule) {
        return scheduleService.create(schedule);
    }

    @PostMapping(path = "/schedules/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    public void finish(@PathVariable String id, @RequestBody @Valid FinishedSchedule finished, BindingResult binding) {

        if (binding.hasErrors()) {
            throw new InvalidPayloadException(binding);
        }

        scheduleService.finish(id, finished);
    }

    @DeleteMapping(path = "/schedules/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable String id) {
        scheduleService.remove(id);
    }
}
