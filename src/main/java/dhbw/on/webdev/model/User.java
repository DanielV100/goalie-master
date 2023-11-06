package dhbw.on.webdev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
@UserDefinition
public class User {
    @Id
    @GeneratedValue
    public long id;
    @Username
    @Column(name = "username")
    public String username;
    @Column(name = "name")
    public String name;
    @Password
    @Column(name = "password")
    public String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Transient
    public List<Goalkeeper> goalkeepers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    @Transient
    public List<Exercise> exercises;


}
