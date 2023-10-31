package dhbw.on.webdev.model;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;

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


}
