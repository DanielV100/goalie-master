package dhbw.on.webdev.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.batik.svggen.font.table.LigatureSet;

import java.time.LocalDate;
import java.util.List;


/**
 * DTO for goalkeeper entity.
 * User from goalkeeper may not be in http-response
 * @param id
 * @param firstname
 * @param lastname
 * @param club
 * @param birthday
 * @param notes
 */
@RegisterForReflection
public record GoalkeeperDTO(long id, String firstname, String lastname, String club, LocalDate birthday, String notes) {
    @Override
    public long id() {
        return id;
    }
}

