package eu.techmoodivns.opi.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;
import java.time.LocalDateTime;

@Document("subjects")
public class Subject {

    @Id
    @JsonProperty(access = READ_ONLY)
    private String id;

    @NotBlank
    @TextIndexed(weight = 2)
    private String name;

    @TextIndexed
    private String notes;

    @JsonProperty(access = READ_ONLY)
    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private Opinion opinion;

    enum Opinion {
        EXCELLENT, GOOD, SO_SO, BAD
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public Opinion getOpinion() {
        return opinion;
    }
}
