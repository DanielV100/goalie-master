package dhbw.on.webdev.model.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

/**
 * DTO for exercise entity.
 * User from exercise may not be in http-response.
 * @param id
 * @param title
 * @param category
 * @param categoryGroup
 * @param numberOfGoalkeepers
 * @param duration
 * @param intensity
 * @param materials
 * @param numbersOfMaterial
 * @param descriptionSteps
 * @param sketch
 * @param note
 */
@RegisterForReflection
public record ExerciseDTO(long id, String title, String category, String categoryGroup, int numberOfGoalkeepers, String duration, int intensity, String materials, String numbersOfMaterial, String descriptionSteps, byte[] sketch, String note) {
    @Override
    public long id() {
        return id;
    }

}
