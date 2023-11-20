package dhbw.on.webdev.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dhbw.on.webdev.model.baseClass.BaseClass;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Goalkeeper entity saved in db.
 * @author daniel
 */
@Entity
@Table(name = "goalkeepers")
public class Goalkeeper extends BaseClass {
    /**** FIELDS ****/
    private String firstname;
    private String lastname;
    private String club;
    @Nullable
    private LocalDate birthday;
    @Nullable
    private String notes;

    /**** FOREIGN_KEYS ****/
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "goalkeepers")
    @JsonIgnore
    @Transient
    private List<TrainingSession> trainingSessions;

    /**** GETTERS AND SETTERS ****/
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    @Nullable
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(@Nullable LocalDate birthday) {
        this.birthday = birthday;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    /**** OVERRIDES ****/
    @Override
    public String toString() {
        return "Goalkeeper{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", club='" + club + '\'' +
                ", birthday=" + birthday +
                ", notes='" + notes + '\'' +
                ", user=" + user +
                ", trainingSessions=" + trainingSessions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goalkeeper that = (Goalkeeper) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(club, that.club) && Objects.equals(birthday, that.birthday) && Objects.equals(notes, that.notes) && Objects.equals(user, that.user) && Objects.equals(trainingSessions, that.trainingSessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, club, birthday, notes, user, trainingSessions);
    }
}
