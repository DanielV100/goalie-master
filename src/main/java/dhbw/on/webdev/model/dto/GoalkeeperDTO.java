package dhbw.on.webdev.model.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDate;

/**
 * DTO for goalkeeper entity.
 * User from goalkeeper may not be in http-response.
 * @author daniel
 */
@RegisterForReflection
public class GoalkeeperDTO {
    /**** FIELD DEFINITION ****/
    private final long id;
    private final String firstname;
    private final String lastname;
    private final String club;
    private final LocalDate birthday;
    private final String notes;

    /**** CONSTRUCTOR ****/

    public GoalkeeperDTO(long id, String firstname, String lastname, String club, LocalDate birthday, String notes) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.club = club;
        this.birthday = birthday;
        this.notes = notes;
    }

    /**** GETTERS ****/
    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getClub() {
        return club;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getNotes() {
        return notes;
    }
}
