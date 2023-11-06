package dhbw.on.webdev.model;
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

    @ElementCollection
    @CollectionTable(name = "materials", joinColumns = @JoinColumn(name = "exercises_id"))
    @Column(name = "material")
    public List<String> materials;

    @ElementCollection
    @CollectionTable(name = "numbers_of_material", joinColumns = @JoinColumn(name = "exercises_id"))
    @Column(name = "number_of_material")
    public List<Integer> numbersOfMaterial;

    @ElementCollection
    @CollectionTable(name = "description_steps", joinColumns = @JoinColumn(name = "exercises_id"))
    @Column(name = "description_step")
    public List<String> descriptionSteps;

    @Column(name = "sketch", columnDefinition = "bytea")
    public byte[] sketch;
    @Column(name = "note")
    public String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

}
