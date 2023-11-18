package dhbw.on.webdev.model.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dhbw.on.webdev.model.baseClass.BaseClass;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "exercises")
public class Exercise extends BaseClass {
    @Transient
    private String sketchDataURL;

    private String title;

    private String category;
    @Column(name = "category_group")
    private String categoryGroup;
    @Column(name = "number_of_goalkeepers")
    private int numberOfGoalkeepers;
    private String duration;

    private int intensity;

    @ElementCollection
    @CollectionTable(name = "materials", joinColumns = @JoinColumn(name = "exercises_id"))
    @Column(name = "material")
    private List<String> materials;

    @ElementCollection
    @CollectionTable(name = "numbers_of_material", joinColumns = @JoinColumn(name = "exercises_id"))
    @Column(name = "number_of_material")
    private List<Integer> numbersOfMaterial;

    @ElementCollection
    @CollectionTable(name = "description_steps", joinColumns = @JoinColumn(name = "exercises_id"))
    @Column(name = "description_step")
    private List<String> descriptionSteps;

    @Column(columnDefinition = "bytea")
    private byte[] sketch;

    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "exercises")
    @JsonIgnore
    @Transient
    private List<TrainingSession> trainingSessions;

    public String getSketchDataURL() {
        return sketchDataURL;
    }

    public void setSketchDataURL(String sketchDataURL) {
        this.sketchDataURL = sketchDataURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public int getNumberOfGoalkeepers() {
        return numberOfGoalkeepers;
    }

    public void setNumberOfGoalkeepers(int numberOfGoalkeepers) {
        this.numberOfGoalkeepers = numberOfGoalkeepers;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<Integer> getNumbersOfMaterial() {
        return numbersOfMaterial;
    }

    public void setNumbersOfMaterial(List<Integer> numbersOfMaterial) {
        this.numbersOfMaterial = numbersOfMaterial;
    }

    public List<String> getDescriptionSteps() {
        return descriptionSteps;
    }

    public void setDescriptionSteps(List<String> descriptionSteps) {
        this.descriptionSteps = descriptionSteps;
    }

    public byte[] getSketch() {
        return sketch;
    }

    public void setSketch(byte[] sketch) {
        this.sketch = sketch;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
}
