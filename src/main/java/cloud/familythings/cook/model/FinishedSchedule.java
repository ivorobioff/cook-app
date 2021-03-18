package cloud.familythings.cook.model;

import javax.validation.constraints.NotBlank;

public class FinishedSchedule {
    @NotBlank
    private String notes;

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
}
