package dhbw.on.webdev.model.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dhbw.on.webdev.model.baseClass.BaseClass;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Exercise entity saved in db.
 * Materials, description steps and number of materials
 * are saved as string but passed as a json list from the client.
 * So they'll get parsed in the getters/setters.
 * @author daniel
 */
@Entity
@Table(name = "exercises")
public class Exercise extends BaseClass {
    /**** TRANSIENT FIELDS ****/
    @Transient
    private String sketchDataURL;

    /**** FIELDS ****/
    private String title;
    private String category;
    @Column(name = "category_group")
    private String categoryGroup;
    @Column(name = "number_of_goalkeepers")
    private int numberOfGoalkeepers;
    private String duration;
    private int intensity;
    private String materials;
    private String numbersOfMaterial;
    private String descriptionSteps;
    @Column(columnDefinition = "bytea")
    private byte[] sketch;
    private String note;

    /**** FOREIGN_KEYS ****/
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(mappedBy = "exercises")
    @JsonIgnore
    @Transient
    private List<TrainingSession> trainingSessions;

    /**** GETTERS AND SETTERS ****/

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
        try {
            return new ObjectMapper().readValue(materials, new TypeReference<List<String>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMaterials(List<String> materials) {
        try {
            this.materials = new ObjectMapper().writeValueAsString(materials);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getNumbersOfMaterial() {
        try {
            return new ObjectMapper().readValue(numbersOfMaterial, new TypeReference<List<Integer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setNumbersOfMaterial(List<Integer> numbersOfMaterial) {
        try {
            this.numbersOfMaterial = new ObjectMapper().writeValueAsString(numbersOfMaterial);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getDescriptionSteps() {
        try {
            return new ObjectMapper().readValue(descriptionSteps, new TypeReference<List<String>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDescriptionSteps(List<String> descriptionSteps) {
        try {
            this.descriptionSteps = new ObjectMapper().writeValueAsString(descriptionSteps);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    /**** OVERRIDES ****/
    @Override
    public String toString() {
        return "Exercise{" +
                "sketchDataURL='" + sketchDataURL + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", categoryGroup='" + categoryGroup + '\'' +
                ", numberOfGoalkeepers=" + numberOfGoalkeepers +
                ", duration='" + duration + '\'' +
                ", intensity=" + intensity +
                ", materials='" + materials + '\'' +
                ", numbersOfMaterial='" + numbersOfMaterial + '\'' +
                ", descriptionSteps='" + descriptionSteps + '\'' +
                ", sketch=" + Arrays.toString(sketch) +
                ", note='" + note + '\'' +
                ", user=" + user +
                ", trainingSessions=" + trainingSessions +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return numberOfGoalkeepers == exercise.numberOfGoalkeepers && intensity == exercise.intensity && Objects.equals(sketchDataURL, exercise.sketchDataURL) && Objects.equals(title, exercise.title) && Objects.equals(category, exercise.category) && Objects.equals(categoryGroup, exercise.categoryGroup) && Objects.equals(duration, exercise.duration) && Objects.equals(materials, exercise.materials) && Objects.equals(numbersOfMaterial, exercise.numbersOfMaterial) && Objects.equals(descriptionSteps, exercise.descriptionSteps) && Arrays.equals(sketch, exercise.sketch) && Objects.equals(note, exercise.note) && Objects.equals(user, exercise.user) && Objects.equals(trainingSessions, exercise.trainingSessions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sketchDataURL, title, category, categoryGroup, numberOfGoalkeepers, duration, intensity, materials, numbersOfMaterial, descriptionSteps, note, user, trainingSessions);
        result = 31 * result + Arrays.hashCode(sketch);
        return result;
    }
}
