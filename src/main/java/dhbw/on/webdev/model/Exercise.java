package dhbw.on.webdev.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "exercises")
public class Exercise {
    @Transient
    public String sketchDataURL;

    @Id
    @GeneratedValue
    public long id;
    @Column(name = "title")
    public String title;
    @Column(name = "category")
    public String category;
    @Column(name = "number_of_goalkeepers")
    public int numberOfGoalkeepers;
    @Column(name = "duration")
    public String duration;
    @Column(name = "intensity")
    public int intensity;
    @Column(name = "number_of_material")
    @ElementCollection
    public List<Integer> number_of_materials;
    @Column(name = "material")
    @ElementCollection
    public List<String> materials;
    @Column(name = "description_steps")
    @ElementCollection
    public List<String> description_steps;

    @Column(name = "sketch", columnDefinition = "bytea")
    public byte[] sketch;
    @Column(name = "note")
    public String note;

    @ManyToOne
    @JoinColumn(name = "users_id")
    public User user;


}
