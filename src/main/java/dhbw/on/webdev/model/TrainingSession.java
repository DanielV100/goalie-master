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
    public long id;

    public String title;

    public LocalDate tDate;

    public String tNotes;

    @ManyToMany
    @JoinTable(
            name = "training_session_goalkeepers",
            joinColumns = @JoinColumn(name = "training_sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "goalkeepers_id")
    )
    public List<Goalkeeper> goalkeepers;

    @Transient
    public List<Long> goalkeeperIds;

    @ManyToMany
    @JoinTable(
            name = "training_session_exercises",
            joinColumns = @JoinColumn(name = "training_sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "exercises_id")
    )
    public List<Exercise> exercises;
    @Transient
    public List<Long> exerciseIds;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
}
