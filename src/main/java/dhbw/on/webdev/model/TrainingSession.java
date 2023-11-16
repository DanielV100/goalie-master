package dhbw.on.webdev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {
    @Id
    @GeneratedValue
    private long id;

    private String title;

    private LocalDate date;

    private String notes;

    @ManyToMany
    @JoinTable(
            name = "training_session_goalkeepers",
            joinColumns = @JoinColumn(name = "training_sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "goalkeepers_id")
    )
    private List<Goalkeeper> goalkeepers;

    @Transient
    private List<Long> goalkeeperIds;

    @ManyToMany
    @JoinTable(
            name = "training_session_exercises",
            joinColumns = @JoinColumn(name = "training_sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "exercises_id")
    )
    private List<Exercise> exercises;
    @Transient
    private List<Long> exerciseIds;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private String dataUrl;

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Goalkeeper> getGoalkeepers() {
        return goalkeepers;
    }

    public void setGoalkeepers(List<Goalkeeper> goalkeepers) {
        this.goalkeepers = goalkeepers;
    }

    public List<Long> getGoalkeeperIds() {
        return goalkeeperIds;
    }

    public void setGoalkeeperIds(List<Long> goalkeeperIds) {
        this.goalkeeperIds = goalkeeperIds;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Long> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<Long> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
