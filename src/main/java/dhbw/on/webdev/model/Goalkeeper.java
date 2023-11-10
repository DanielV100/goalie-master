package dhbw.on.webdev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "goalkeepers")
public class Goalkeeper {
    @Id
    @GeneratedValue
    public long id;

    @Column(name = "firstname")
    public String firstname;
    @Column(name = "lastname")
    public String lastname;
    @Column(name = "club")
    public String club;
    @Column(name = "birthday")
    @Nullable
    public LocalDate birthday;

    @Column(name = "notes")
    @Nullable
    public String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User user;

    @OneToMany(mappedBy = "goalkeeper")
    @JsonIgnore
    @Transient
    public List<TrainingSession> trainingSessions;


}
