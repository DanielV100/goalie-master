package dhbw.on.webdev.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;


}
