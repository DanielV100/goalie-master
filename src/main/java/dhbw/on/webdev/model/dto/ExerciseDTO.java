package dhbw.on.webdev.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


/**
 * DTO for exercise entity.
 * User from exercise may not be in http-response.
 * In a response sketch should be a dataUrl - so client hasn't to parse it:
 * getSketch()
 */
@RegisterForReflection
public class ExerciseDTO {
    /**** DTO-FIELDS-FOR-REFLECTION ****/
    final private long id;
    final private String title;
    final private String category;
    final private String categoryGroup;
    final private int numberOfGoalkeepers;
    final private String duration;
    final private int intensity;
    final private String materialsAsString;
    final private String numbersOfMaterialAsString;
    final private String descriptionStepsAsString;
    final private byte[] sketch;
    final private String note;

    /**** FIELDS FOR MAKING RESPONSE EASIER ****/
    private List<Integer> numbersOfMaterial;
    private List<String> materials;
    private List<String> descriptionSteps;

    /**** CONSTRUCTOR ****/
    public ExerciseDTO(long id, String title, String category, String categoryGroup, int numberOfGoalkeepers, String duration, int intensity, @ProjectedFieldName("materials") String materialsAsString, @ProjectedFieldName("numbersOfMaterial") String numbersOfMaterialAsString, @ProjectedFieldName("descriptionSteps") String descriptionStepsAsString, byte[] sketch, String note) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.categoryGroup = categoryGroup;
        this.numberOfGoalkeepers = numberOfGoalkeepers;
        this.duration = duration;
        this.intensity = intensity;
        this.materialsAsString = materialsAsString;
        this.numbersOfMaterialAsString = numbersOfMaterialAsString;
        this.descriptionStepsAsString = descriptionStepsAsString;
        this.sketch = sketch;
        this.note = note;

        //mapping jsons strings to List<T> - so, client hasn't to parse
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.descriptionSteps = mapper.readValue(descriptionStepsAsString, new TypeReference<List<String>>(){});
            this.materials = mapper.readValue(materialsAsString, new TypeReference<List<String>>(){});
            //you can't directly parse a string to a list of integer - so you need a temp list
            List<String> numberOfMaterialsTemp = mapper.readValue(numbersOfMaterialAsString, new TypeReference<List<String>>(){});
            //...then you can parse the strings to integer
            this.numbersOfMaterial = numberOfMaterialsTemp.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

        } catch (JsonProcessingException exception) {
            Log.error("Couldn't parse Json data to list", exception);
            throw new RuntimeException(exception);
        }

    }

    /**** GETTER AND SETTER ****/
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public int getNumberOfGoalkeepers() {
        return numberOfGoalkeepers;
    }

    public String getDuration() {
        return duration;
    }

    public int getIntensity() {
        return intensity;
    }

    public String getMaterialsAsString() {
        return materialsAsString;
    }

    public String getNumbersOfMaterialAsString() {
        return numbersOfMaterialAsString;
    }

    /**
     * Client should directly receive data url and not the byte array
     * @return data url from byte array
     */
    public String getSketch() {
        if(sketch != null) {
            final String base64String = Base64.getEncoder().encodeToString(sketch);
            return "data:image/png;base64," + base64String;
        }
        return "";
    }

    public String getNote() {
        return note;
    }

    public String getDescriptionStepsAsString() {
        return descriptionStepsAsString;
    }

    public List<Integer> getNumbersOfMaterial() {
        return numbersOfMaterial;
    }

    public void setNumbersOfMaterial(List<Integer> numbersOfMaterial) {
        this.numbersOfMaterial = numbersOfMaterial;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getDescriptionSteps() {
        return descriptionSteps;
    }

    public void setDescriptionSteps(List<String> descriptionSteps) {
        this.descriptionSteps = descriptionSteps;
    }
}
