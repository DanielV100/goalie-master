package dhbw.on.webdev.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dhbw.on.webdev.model.baseClass.BaseClass;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@UserDefinition
public class User extends BaseClass {
    @Username
    private String username;
    private String name;
    @Password
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Transient
    private List<Goalkeeper> goalkeepers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Transient
    private List<Exercise> exercises;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Transient
    private List<TrainingSession> trainingSessions;

    public User() {
    }

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Goalkeeper> getGoalkeepers() {
        return goalkeepers;
    }

    public void setGoalkeepers(List<Goalkeeper> goalkeepers) {
        this.goalkeepers = goalkeepers;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(goalkeepers, user.goalkeepers) && Objects.equals(exercises, user.exercises) && Objects.equals(trainingSessions, user.trainingSessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, password, goalkeepers, exercises, trainingSessions);
    }
}
